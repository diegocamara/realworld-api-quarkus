package org.example.realworldapi.domain.service.impl;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.exception.ExistingEmailException;
import org.example.realworldapi.domain.exception.InvalidPasswordException;
import org.example.realworldapi.domain.exception.UserNotCreatedException;
import org.example.realworldapi.domain.exception.UserNotFoundException;
import org.example.realworldapi.domain.repository.UsersRepository;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.web.exception.ResourceNotFoundException;
import org.hibernate.Hibernate;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional
    public User create(String username, String email, String password) {

        checkExistingEmail(email);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email.toUpperCase().trim());
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setToken(UUID.randomUUID().toString());

        return usersRepository.create(user).orElseThrow(UserNotCreatedException::new);
    }

    private void checkExistingEmail(String email) {
        if(usersRepository.exists(email)){
            throw new ExistingEmailException();
        }
    }

    @Override
    @Transactional
    public User login(String email, String password) {

        Optional<User> resultUser = usersRepository.findByEmail(email);

        if(!resultUser.isPresent()){
            throw new UserNotFoundException();
        }

        if(isPasswordInvalid(password, resultUser.get())){
           throw new InvalidPasswordException();
        }

        return resultUser.get();
    }

    @Override
    @Transactional
    public User findById(Long id) {
        return usersRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @Transactional
    public User update(User user) {
        return usersRepository.update(user);
    }


    private boolean isPasswordInvalid(String password, User user) {
        return !BCrypt.checkpw(password, user.getPassword());
    }

}

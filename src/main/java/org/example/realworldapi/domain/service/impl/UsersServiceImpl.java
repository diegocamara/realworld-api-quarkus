package org.example.realworldapi.domain.service.impl;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.exception.ConflictException;
import org.example.realworldapi.domain.exception.UnauthorizedException;
import org.example.realworldapi.domain.repository.UsersRepository;
import org.example.realworldapi.domain.service.UsersService;
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

        return usersRepository.create(user);
    }

    private void checkExistingEmail(String email) {
        if(usersRepository.exists(email)){
            throw new ConflictException();
        }
    }

    @Override
    @Transactional
    public User login(String email, String password) {

        Optional<User> resultUser = usersRepository.findByEmail(email);

        if(!resultUser.isPresent() || isPasswordInvalid(password, resultUser.get())){
            throw new UnauthorizedException();
        }

        return resultUser.get();
    }

    private boolean isPasswordInvalid(String password, User user) {
        return !BCrypt.checkpw(password, user.getPassword());
    }

}

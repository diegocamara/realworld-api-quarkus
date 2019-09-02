package org.example.realworldapi.domain.service.impl;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.UsersRepository;
import org.example.realworldapi.domain.service.UsersService;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
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

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setToken(UUID.randomUUID().toString());

        return usersRepository.create(user);
    }
}

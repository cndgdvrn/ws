package com.boilerplate.ws.user;

import com.boilerplate.ws.email.EmailService;
import com.boilerplate.ws.user.exception.ActivationMailException;
import com.boilerplate.ws.user.exception.InvalidTokenException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.UUID;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional(rollbackOn = MailException.class)
    public void save(User user) {
        try {
            String activationToken = UUID.randomUUID().toString();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActivationToken(activationToken);
            userRepository.saveAndFlush(user);
            emailService.sendUserActivationEmail(user);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Username or email already exists");
        } catch (MailException ex) {
            throw new ActivationMailException();
        }
    }


    public void activateUser(String token) {
        User user = userRepository.findByActivationToken(token);
        if(user != null){
            user.setActive(true);
            user.setActivationToken(null);
            userRepository.save(user);
            return;
        }
        throw new InvalidTokenException(token);
    }
}

package com.boilerplate.ws.user;

import com.boilerplate.ws.email.EmailService;
import com.boilerplate.ws.user.dto.UserUpdate;
import com.boilerplate.ws.user.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.Optional;
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
        if (user != null) {
            user.setActive(true);
            user.setActivationToken(null);
            userRepository.save(user);
            return;
        }
        throw new InvalidActivationTokenException(token);
    }

    public Page<User> getUsers(Pageable pageable, User currentUser) {
        if(currentUser == null) return userRepository.findAll(pageable);
        return userRepository.findByIdNot(currentUser.getId(), pageable);
//        Page<UserDTO> userDTOS = userRepository.findAll(pageable).map(UserDTO::new);
//
//        List<UserDTO> filteredList = userDTOS.stream()
//                .filter(userDTO -> !userDTO.getId().equals(currentUser != null ? currentUser.getId() : -1))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(filteredList, pageable, userDTOS.getTotalElements() - 1);
    }

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundByIdException(id);
        }
        return user.get();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(Long id, UserUpdate userUpdate,User currentUser) {

        if (userUpdate == null) throw new RuntimeException("UserUpdate object is null");
        if(currentUser == null || !Objects.equals(currentUser.getId(), id)){
            throw new AuthorizationException();
        }

        User userInDB = getUser(id);
        userInDB.setUsername(userUpdate.getUsername());
        return userRepository.save(userInDB);

    }
}

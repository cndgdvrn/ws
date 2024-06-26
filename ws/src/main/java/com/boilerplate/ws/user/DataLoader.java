package com.boilerplate.ws.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("development")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> userInDB = userRepository.findById(1L);
        if(userInDB.isPresent()){
            return;
        }
        for(int i=1; i<20; i++){
            User user = new User();
            user.setUsername("user"+i);
            user.setEmail("user"+i+"@mail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setActive(true);
            if(i==2){
                user.setActive(false);
            }
            user.setActivationToken(null);
            userRepository.save(user);
        }
    }
}

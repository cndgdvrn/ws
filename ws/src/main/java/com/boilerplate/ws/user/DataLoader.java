package com.boilerplate.ws.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("development")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        for(int i=1; i<20; i++){
            User user = new User();
            user.setUsername("user"+i);
            user.setEmail("user"+i+"@mail.com");
            user.setPassword(passwordEncoder.encode("P4ssword"));
            user.setActive(true);
            user.setActivationToken(null);
            userRepository.save(user);
        }
    }
}

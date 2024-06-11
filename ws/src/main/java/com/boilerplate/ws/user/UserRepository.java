package com.boilerplate.ws.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    User findByUsername(String username);
    User findByActivationToken(String token);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}

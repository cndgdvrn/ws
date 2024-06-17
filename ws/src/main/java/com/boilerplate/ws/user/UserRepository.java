package com.boilerplate.ws.user;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    User findByUsername(String username);
    User findByActivationToken(String token);
    Page<User> findByIdNot(Long id, Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}

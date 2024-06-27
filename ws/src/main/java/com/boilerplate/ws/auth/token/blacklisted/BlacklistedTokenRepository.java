package com.boilerplate.ws.auth.token.blacklisted;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String> {
}

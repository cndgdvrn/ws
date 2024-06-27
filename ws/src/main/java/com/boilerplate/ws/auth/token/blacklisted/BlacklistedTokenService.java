package com.boilerplate.ws.auth.token.blacklisted;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistedTokenService {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    public void save(BlacklistedToken blacklistedToken) {
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokenRepository.existsById(token);
    }
}

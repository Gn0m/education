package com.example.demo.jwt.util;

import com.example.demo.jwt.model.UserAttempts;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BlockedAuthenticationProvider extends DaoAuthenticationProvider {

    public final UserAttemptsService service;

    public BlockedAuthenticationProvider(UserAttemptsService service, UserDetailsService userDetailsService, PasswordEncoder encoder) {
        super.setUserDetailsService(userDetailsService);
        super.setPasswordEncoder(encoder);
        this.service = service;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        try {

            Authentication auth = super.authenticate(authentication);

            service.resetFailAttempts(authentication.getName());

            return auth;

        } catch (BadCredentialsException e) {

            service.updateFailAttempts(authentication.getName());

            throw e;
        } catch (LockedException e) {

            String error = "";
            UserAttempts userAttempts =
                    service.getUserAttempts(authentication.getName());

            if (userAttempts != null) {
                Date lastAttempts = userAttempts.getLastModifier();
                error = "User account is locked! Username: "
                        + authentication.getName() + " Last Attempts : " + lastAttempts;
            } else {
                error = e.getMessage();
            }

            throw new LockedException(error);
        }
    }
}

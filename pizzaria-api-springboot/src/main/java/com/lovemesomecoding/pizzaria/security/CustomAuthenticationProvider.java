package com.lovemesomecoding.pizzaria.security;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.lovemesomecoding.pizzaria.entity.user.User;
import com.lovemesomecoding.pizzaria.entity.user.UserDAO;
import com.lovemesomecoding.pizzaria.entity.user.UserStatus;
import com.lovemesomecoding.pizzaria.utils.PasswordUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * CustomAuthenticationProvider
 * 
 * @author fkaveinga
 *
 */
@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDAO userDAO;

    /**
     * Authenticate user by credentials
     * 
     * @author fkaveinga
     * @return Authentication
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("authenticate(...)");
        Map<String, String> details = (Map) authentication.getDetails();
        String type = details.get("type");
        log.debug("authentication type: {}", type);

        switch (type) {
            case "password":
                String email = authentication.getPrincipal().toString();
                String password = authentication.getCredentials().toString();
                return loginWithPassword(email, password);
            default:
                throw new BadCredentialsException("Username or password is invalid");
        }
    }

    private Authentication loginAdminWithPassword(String email, String password) {
        log.debug("loginAdminWithPassword({})", email);
        User user = null;
        UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(user.getEmail(), password);
        userAuthToken.setDetails(user);
        return userAuthToken;
    }

    private Authentication loginWithPassword(String email, String password) {
        log.debug("loginWithPassword({})", email);
        User user = userDAO.getByEmail(email);

        if (user == null) {
            log.debug("user not found");
            throw new UsernameNotFoundException("Email or password is invalid");
        }

        log.debug("member found for {}", email);

        int invalidPasswordCounter = Optional.ofNullable(user.getInvalidPasswordCounter()).orElse(0);

        if (user.getPassword() == null || !PasswordUtils.verify(password, user.getPassword())) {
            log.debug("Password={} is invalid", password);

            user.setInvalidPasswordCounter(invalidPasswordCounter + 1);
            this.userDAO.save(user);

            throw new BadCredentialsException("Email or password is invalid");
        }

        if (UserStatus.isActive(user.getStatus()) == false) {
            log.debug("Your account has beeen deactivated");
            throw new DisabledException("Your account has beeen deactivated");
        }

        // =============== Valid Username and Password =============

        if (invalidPasswordCounter > 0) {
            user.setInvalidPasswordCounter(0);
            this.userDAO.save(user);
        }

        UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        userAuthToken.setDetails(user);

        return userAuthToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}

package dev.J.RepositoryForFamilies.Users;

import lombok.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
@Setter
@Getter
@RequiredArgsConstructor

public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Users user = (Users) userService.loadUserByUsername(authentication.getName());
        if(passwordEncoder.matches((String) authentication.getCredentials(), userService.loadUserByUsername(authentication.getName()).getPassword())) {
            return new EmailPasswordAuthenticationToken(
                    authentication.getName(),
                    null,
                    true,
                    Collections.emptyList(),
                    user
            );
        }
        return new EmailPasswordAuthenticationToken(
                authentication.getName(),
                null,
                false,
                Collections.emptyList(),
                null
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

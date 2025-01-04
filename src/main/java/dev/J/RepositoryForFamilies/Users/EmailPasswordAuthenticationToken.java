package dev.J.RepositoryForFamilies.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@ToString
@Getter
public class EmailPasswordAuthenticationToken implements Authentication {

    private String email;

    private String password;

    private boolean isAuthenticated;

    private List<GrantedAuthority> authorities;

    private Users user;

    public EmailPasswordAuthenticationToken(String email, String password, boolean isAuthenticated, List<GrantedAuthority> authorities, Users user) {
        this.email = email;
        this.password = password;
        this.isAuthenticated = isAuthenticated;
        this.authorities = authorities;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Do not set the authenticated flag.");
    }

    @Override
    public String getName() {
        return email;
    }
}

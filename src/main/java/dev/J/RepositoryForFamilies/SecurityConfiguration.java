package dev.J.RepositoryForFamilies;

import dev.J.RepositoryForFamilies.Groups.GroupsRepository;
import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationProvider;
import dev.J.RepositoryForFamilies.Users.UserRepository;
import dev.J.RepositoryForFamilies.Users.UserService;
import org.apache.catalina.Group;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, GroupsRepository groupRepo) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authorize) -> { authorize
                    .requestMatchers("/auth/api/**").permitAll()
                    .anyRequest().authenticated();
            })
            .addFilterAfter(new GroupFilter(groupRepo),AuthorizationFilter.class)
            .addFilterAfter(new AdminFilter(groupRepo),GroupFilter.class);

        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userService, PasswordEncoder encoder) {
        EmailPasswordAuthenticationProvider provider = new EmailPasswordAuthenticationProvider(userService,encoder);
        return new ProviderManager(provider);
    }



    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return new UserService(userRepo);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

package dev.J.RepositoryForFamilies.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:3000"},allowCredentials = "true")
@RestController
public class UserController
{
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/auth/api/login")
    public ResponseEntity<Void> login(@RequestBody LoginBody body, HttpServletRequest request, HttpServletResponse response){
        Authentication auth = new EmailPasswordAuthenticationToken(
                body.getEmail(),
                body.getPassword(),
                false,
                null,
                null
                );
        try {
            auth = authenticationManager.authenticate(auth);
            SecurityContext context =  SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context,request,response);
        }
        catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/api/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpBody body){
        if(userService.userExists(body.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else if(body.getEmail().isBlank()      ||
                 body.getPassword().isBlank()   ||
                 body.getFirstName().isBlank()  ||
                 body.getLastName().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"reason\" : \"all fields must be filled\"}");
        }


        String password = passwordEncoder.encode(body.getPassword());
        Users newUser = new Users(body.getEmail(),password,body.getFirstName(),body.getLastName());
        userService.saveUser(newUser);
        return ResponseEntity
                .ok()
                .body("{\"status\" : \"user created\"}");
    }

    @GetMapping("/authed/loggedin")
    public ResponseEntity<Void> loggedin(){
        return ResponseEntity
                .ok()
                .build();
    }
}

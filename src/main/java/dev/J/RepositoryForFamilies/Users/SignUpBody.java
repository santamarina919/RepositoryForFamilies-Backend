package dev.J.RepositoryForFamilies.Users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpBody {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}

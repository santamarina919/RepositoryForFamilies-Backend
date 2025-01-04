package dev.J.RepositoryForFamilies.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginBody {

    private String email;

    private String password;

}

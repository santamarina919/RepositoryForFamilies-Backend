package dev.J.RepositoryForFamilies.Users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findById(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public Users userExists(String email) {
        return userRepo.findById(email).orElse(null);
    }

    public void saveUser(Users newUser) {
        userRepo.save(newUser);
    }

    public <T> List<? extends T> fetchUserInfo(UUID groupId, Class<T> clazz) {
        return (List<? extends T>) userRepo.findUsersInGroup(groupId,clazz.getClass());
    }
}

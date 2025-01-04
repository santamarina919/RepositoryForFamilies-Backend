package dev.J.RepositoryForFamilies.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface UserRepository extends JpaRepository<Users,String>
{
    //TODO implement
    @Query(value =
            "SELECT email,users.first_name as firstName,users.last_name as lastName " +
                    "FROM users " +
                    "INNER JOIN members m on users.email = m.user_id " +
                    "WHERE m.type = 'UNAUTHORIZED' AND m.group_id = ?1",
            nativeQuery = true )
    List<UserEmailNameOnly> fetchUnapprovedMembers(UUID group_id);
}

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
    <T> List<T> fetchUnapprovedMembers(UUID group_id, Class<T> clazz);


    @Query(
            value = "SELECT users.email, users.password, users.first_name, users.last_name " +
                    "FROM users " +
                    "INNER join members ON members.user_id = users.email " +
                    "WHERE members.group_id = ?1",
            nativeQuery = true )
    <T> List<T> findUsersInGroup(UUID groupId, Class<T> clazz);
}

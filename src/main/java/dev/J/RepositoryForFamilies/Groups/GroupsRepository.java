package dev.J.RepositoryForFamilies.Groups;

import dev.J.RepositoryForFamilies.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GroupsRepository extends JpaRepository<Groups,UUID>
{
    @Query(value =
            "SELECT groups.group_id AS id, groups.group_name as name, members.type " +
            "FROM groups " +
            "INNER JOIN members USING (group_id) " +
            "WHERE members.user_id = ?1",
    nativeQuery = true)
    List<GroupCardDTO> groupsWhereUserIsMember(String userId);


    @Modifying
    @Query(value = "INSERT INTO groups (group_id,group_name, owner) VALUES (?1,?2,?3) ",
    nativeQuery = true)
    void createGroup(UUID groupId, String group_name, String email);

    @Modifying
    @Query(value =
            "INSERT INTO members (group_id, user_id,type) VALUES (?1,?2,?3)",
    nativeQuery = true)
    void addGroupMember(UUID group_id, String member_id, String type);


    //TODO: make sure this works
    @Modifying
    @Query(value = "UPDATE members " +
            "SET type = ?3 " +
            "WHERE user_id = ?2 AND " +
            "group_id = ?1",
    nativeQuery = true)
    void updateMemberType(UUID group_id, String member_id, String type);

    @Query(value =  "SELECT members.type " +
                    "FROM members " +
                    "WHERE user_id = ?2 AND " +
                    "group_id = ?1",
    nativeQuery = true)
    UserType fetchMemberType(UUID group_id, String member_id);

    //TODO ensure this method works
    @Modifying
    @Query(value = "DELETE FROM members " +
            "WHERE group_id = ?1 AND user_id = ?2",
    nativeQuery = true)
    void removeMember(UUID groupId, String email);

    @Query(value =
            "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM members " +
                "WHERE members.group_id = ?1 AND members.user_id = ?2 " +
            ")",
        nativeQuery = true)
    boolean isMember(UUID groupId, String email);

    <T> T findById(UUID id,Class<T> clazz);

}

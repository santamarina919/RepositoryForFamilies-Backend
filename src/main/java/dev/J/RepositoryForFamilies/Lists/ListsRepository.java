package dev.J.RepositoryForFamilies.Lists;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListsRepository extends CrudRepository<Lists, ListId> {

    @Query(value =
            "SELECT * " +
            "FROM lists " +
            "WHERE lists.group_id = ?1",
    nativeQuery = true)
    <T> List<T> findAll(UUID groupID, Class<T> type);


    @Modifying
    @Query(
            value = "INSERT INTO lists (list_id, group_id,list_name, item_domain, expiration,all_admin) " +
                    "VALUES (?1,?2,?3,?4,?5,?6); ",
            nativeQuery = true
    )
    void createShoppingList(UUID listId, UUID groupID, String name, String itemDomain, LocalDate expiration,boolean allAdmin);

    @Modifying
    @Query(value = "INSERT INTO list_roles (list_id, group_id,user_id,role) " +
                    "VALUES (?1,?2,?3,?4)",
    nativeQuery = true)
    void setUserRole(UUID listId, UUID groupId,String userId, String role);

    @Query(value =
            "SELECT list_roles.role " +
            "FROM list_roles " +
            "WHERE list_id = ?1 AND user_id = ?2",
            nativeQuery = true)
    ListRole fetchListRole(UUID listId, String email);

    Optional<Lists> findByListId(UUID listId);




    @Modifying
    @Query(value =
            "UPDATE item " +
            "SET approved = true " +
            "WHERE item_id = ?1",
            nativeQuery = true)
    void approveItem(UUID itemId);

    <T> List<T> findByGroupId(UUID groupID, Class<T> type);

}

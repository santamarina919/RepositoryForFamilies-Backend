package dev.J.RepositoryForFamilies.Groups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member,Member.MemberKey> {
    @Query(value =
            "SELECT * " +
            "FROM members " +
            "WHERE members.group_id = ?1",
            nativeQuery = true)
    List<Member> fetchMembers(UUID groupId);
}

package dev.J.RepositoryForFamilies.Groups;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@IdClass(Member.MemberKey.class)
@Entity(name = "members")
public class Member {

    public static record MemberKey(UUID groupId, String userId){}

    @Id
    @Column(name = "group_id")
    private UUID groupId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MemberType memberType;

}

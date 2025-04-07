package dev.J.RepositoryForFamilies.Groups;

import dev.J.RepositoryForFamilies.Users.Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Entity(name = "groups")
public class Groups
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    private UUID id;

    @Column(name = "group_name")
    private String name;

    private String owner;



}

package dev.J.RepositoryForFamilies.Lists;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lists")
@IdClass(ListId.class)
public class Lists {
    @Id @GeneratedValue(strategy = GenerationType.UUID)  @Column(name = "list_id")
    private UUID listId;

    @Id @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "list_name")
    private String name;

    @Column(name = "item_domain")
    private String itemDomain;

    @Column
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private LocalDate expiration;

    @Column(name = "all_admin")
    private boolean allAdmins;


}

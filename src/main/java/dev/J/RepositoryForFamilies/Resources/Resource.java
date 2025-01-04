package dev.J.RepositoryForFamilies.Resources;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Resource {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID resourceId;

    private String owner;

    private String name;

    private String description;

}

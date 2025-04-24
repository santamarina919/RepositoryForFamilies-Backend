package dev.J.RepositoryForFamilies.Kitchen;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Kitchen_Item")
public class KitchenItem {
    @Id
    @Column(name = "name")
    private String name;
}

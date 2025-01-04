package dev.J.RepositoryForFamilies.Lists;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ItemId.class)
@Entity(name = "item")
public class Item {

    @Column(name = "item_id")
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemId;

    @Column(name = "list_id")
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID listId;

    @Column(name = "user_id")
    private String email;

    private String name;

    private double price;

    private String url;

    private boolean approved;
}

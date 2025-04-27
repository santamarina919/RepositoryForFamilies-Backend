package dev.J.RepositoryForFamilies.Kitchen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenItemRepository extends JpaRepository<KitchenItem,String> {
}

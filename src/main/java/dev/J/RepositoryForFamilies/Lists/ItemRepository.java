package dev.J.RepositoryForFamilies.Lists;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends CrudRepository<Item, ItemId> {
    <T> List<T> findByListId(UUID listId, Class<T> type);

    <T> Optional<T> findByItemId(UUID itemId, Class<T> type);

}



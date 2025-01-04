package dev.J.RepositoryForFamilies.Lists;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ListService {
    private final ListsRepository listRepo;

    private final ItemRepository itemRepo;


    public <T> List<T> findAll(UUID groupId, Class<T> type){

        return listRepo.findAll(groupId, type);


    }

    @Transactional
    public UUID saveNewList(UUID groupId, String name, String itemDomain, LocalDate expiration, boolean allAdmin,String creatorEmail) {
        UUID newListId = UUID.randomUUID();
        listRepo.createShoppingList(newListId,groupId, name, itemDomain, expiration,allAdmin);
        if(!allAdmin) {
            setUserRole(newListId, groupId, creatorEmail, ListRole.ADMIN.name());
        }
        return newListId;
    }
    @Transactional
    public void setUserRole(UUID listId, UUID groupId, String email, String role){
        listRepo.setUserRole(listId, groupId, email, role);
    }

    @Transactional
    public void addItem(String email, ListsController.AddItemBody body){
        boolean itemApproved = false;
        Lists shoppingList = listRepo.findByListId(body.listId()).orElseThrow(() -> new RuntimeException("Cannot find list"));
        ListRole role = listRepo.fetchListRole(body.listId(),email);
        if(shoppingList.isAllAdmins() || role == ListRole.ADMIN) {
            itemApproved = true;
        }

        UUID itemId = UUID.randomUUID();
        Item newItem = new Item(itemId,body.listId(),email,body.name(),body.price(),body.url(),itemApproved);
        itemRepo.save(newItem);


    }


    public <T> List<T> allItemsIntList(UUID listId, Class<T> type){
        return itemRepo.findByListId(listId, type);
    }


    public boolean removeItem(ListsController.RemoveItemBody body, UUID groupId, String email) {
        UUID itemId = UUID.fromString(body.itemId());
        UUID listId = UUID.fromString(body.listId());
        //Can just search by item id
        ItemId id = new ItemId(itemId,listId);
        Item item = itemRepo.findById(id).orElseThrow();
        ListRole role = listRepo.fetchListRole(listId,email);
        if(!item.getEmail().equals(email) && !(role == ListRole.ADMIN)) {
            return false;
        }
        itemRepo.deleteById(id);
        return true;
    }

    @Transactional
    public boolean approveItem(String email, ListsController.ApproveItemBody body) {
        Lists list = listRepo.findByListId(body.listId()).orElseThrow();
        ListRole role = listRepo.fetchListRole(body.listId(),email);
        if(!(role == ListRole.ADMIN) && !list.isAllAdmins()) {
            return false;
        }

        listRepo.approveItem(UUID.fromString(body.itemId()));
        return true;
    }
}

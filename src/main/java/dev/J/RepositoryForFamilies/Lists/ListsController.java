package dev.J.RepositoryForFamilies.Lists;

import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
@RestController
public class ListsController
{

    private final ListService listService;

    @GetMapping("/lists/api/member/allshoppinglists")
    public List<ListNameAndExpirationOnly> allShoppingLists(@RequestParam("groupId") String groupIdStr){
        UUID groupId = UUID.fromString(groupIdStr);
        return listService.findAll(groupId, ListNameAndExpirationOnly.class);
    }

    @GetMapping("/lists/api/member/items")
    public List<Item> allItemsInShoppingLists(@RequestParam("groupId") String groupIdStr,
                                              @RequestParam("listId") String listIdStr){
        UUID groupId = UUID.fromString(groupIdStr);
        UUID listId = UUID.fromString(listIdStr);
        return listService.allItemsIntList(listId,Item.class);
    }


    public record CreateShoppingListBody(String name,
                                         @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate expiration,
                                         String domainURL,
                                         boolean allAdmin){}

    @PostMapping("lists/api/member/createshoppinglist")
    public void createShoppingList(EmailPasswordAuthenticationToken auth,
                                   @RequestBody CreateShoppingListBody body,
                                   @RequestParam("groupId") String groupIdStr){
        UUID groupId = UUID.fromString(groupIdStr);
        listService.saveNewList(groupId,body.name(),body.domainURL(),body.expiration(),body.allAdmin(), auth.getEmail());

    }

    public record AddItemBody(UUID listId, String name, double price, String url){}

    @PostMapping("lists/api/member/additem")
    public void addItemToShoppingList(EmailPasswordAuthenticationToken auth,
                                      AddItemBody body,
                                      @RequestParam("groupId") String groupIdStr){
        listService.addItem(auth.getEmail(), body);
    }

    public record RemoveItemBody(String listId, String itemId){}

    @PostMapping("lists/api/member/deleteitem")
    public ResponseEntity<Void> deleteItemFromShoppingList(EmailPasswordAuthenticationToken auth,
                                                     RemoveItemBody body,
                                                     @RequestParam("groupId") String groupIdStr){
        UUID groupId = UUID.fromString(groupIdStr);
        boolean wasRemoved = listService.removeItem(body,groupId,auth.getEmail());
        return wasRemoved ?
                ResponseEntity
                        .ok()
                        .build() :
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
    }

    public record ApproveItemBody(UUID listId, String itemId){}

    @PostMapping("lists/api/member/approveitem")
    public ResponseEntity<Void> approveItem(EmailPasswordAuthenticationToken auth,
                                              @RequestParam("groupId") String groupIdStr,
                                              ApproveItemBody body){
        boolean approvalSuccess = listService.approveItem(auth.getEmail(), body);
        return approvalSuccess ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

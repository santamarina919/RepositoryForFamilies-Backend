package dev.J.RepositoryForFamilies.Kitchen;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
@RestController
public class KitchenController {

    private final KitchenService kitchenService;

    @GetMapping("kitchen/api/member/allmeals")
    public Collection<FinalMealDTO> getAllMeals(@RequestParam UUID groupId){
        return kitchenService.allMeals(groupId);
    }

    @GetMapping("kitchen/api/member/allmealssimple")
    public List<MealsNameOnly> getAllMealsSimple(@RequestParam UUID groupId){
        return kitchenService.allMeals(groupId, MealsNameOnly.class);
    }

    public record CreateMealBody(String mealName, double timeToPrepare,
                                 List<Ingredient.IngredientDetails> ingredients, List<SubMeal.MealDetails> subnMeals, List<DirectionDetails> directions){}

    @PostMapping("kitchen/api/member/createmeal")
    public void createMeal(@RequestParam UUID groupId, @RequestBody CreateMealBody body){
        kitchenService.createMeal(groupId,body.mealName,body.timeToPrepare,body.ingredients,body.subnMeals,body.directions);
    }

    @PostMapping("kitchen/api/admin/member/deletemeal")
    public void deleteMeal(@RequestParam UUID groupId, String mealName){
        kitchenService.deleteMeal(mealName);
    }

    @PostMapping("kitchen/api/member/createItems")
    public void createKitchenItem(@RequestParam UUID groupId, @RequestBody List<String> items){
        items.forEach(kitchenService::createKitchenItem);
    }

    @PostMapping("kitchen/api/member/createItem")
    public void createKitchenItem(@RequestParam UUID groupId, @RequestBody String itemName){
        kitchenService.createKitchenItem(itemName);
    }


    @PostMapping("kitchen/api/admin/member/deleteItems")
    public void deleteKitchenItems(@RequestParam UUID groupId, @RequestBody List<String> items){
        items.forEach(kitchenService::deleteKitchenItem);
    }

    @PostMapping("kitchen/api/admin/member/deleteItem")
    public void deleteKitchenItem(@RequestParam UUID groupId, @RequestBody String itemName){
        kitchenService.deleteKitchenItem(itemName);
    }

}

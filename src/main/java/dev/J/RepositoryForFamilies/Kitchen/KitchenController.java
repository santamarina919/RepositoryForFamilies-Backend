package dev.J.RepositoryForFamilies.Kitchen;

import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
@RestController
public class KitchenController {

    private final KitchenService kitchenService;

    @GetMapping("kitchen/api/member/allmeals")
    public Collection<FullMealDTO> getAllMeals(@RequestParam UUID groupId){
        return kitchenService.allMeals(groupId);
    }

    @GetMapping("kitchen/api/member/ingredients")
    public List<KitchenItem> getAllItems(@RequestParam UUID groupId){
        return kitchenService.allItems(groupId);
    }

    @GetMapping("kitchen/api/member/mealcount")
    public Integer getMealCount(@RequestParam UUID groupId){
        return kitchenService.mealCount(groupId);
    }

    @GetMapping("kitchen/api/member/allmealssimple")
    public List<MealsNameOnly> getAllMealsSimple(@RequestParam UUID groupId){
        return kitchenService.allMeals(groupId, MealsNameOnly.class);
    }

    public record CreateMealBody(String mealName, double timeToPrepare,
                                 List<Ingredient.IngredientDetails> ingredients, List<DirectionDetails> directions){}

    @PostMapping("kitchen/api/member/createmeal")
    public void createMeal(@RequestParam UUID groupId, @RequestBody CreateMealBody body){
        kitchenService.createMeal(groupId,body.mealName,body.timeToPrepare,body.ingredients,body.directions);
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

    @GetMapping("kitchen/api/member/meal")
    public FullMealDTO getMeal(@RequestParam UUID groupId, @RequestParam String mealId){
        return kitchenService.getMeal(mealId);
    }

    @GetMapping("kitchen/api/member/item")
    public KitchenItem getItem(@RequestParam UUID groupId, @RequestParam String itemName){
        return kitchenService.getItem(itemName);
    }

    @PostMapping("kitchen/api/member/votemeal")
    public void voteMeal(EmailPasswordAuthenticationToken authentication, @RequestParam UUID groupId, @RequestParam String mealId, @RequestParam LocalDate date, @RequestParam MealType type){
        kitchenService.voteMeal(authentication.getEmail(), date,type,mealId, groupId);
    }

    @GetMapping("kitchen/api/member/mealplanoutcome")
    public List<List<VotedMeal>> getMealPlanOutlook(@RequestParam UUID groupId){

        LocalDate todaysDate = LocalDate.now();

        List<List<VotedMeal>> mealPlans = new ArrayList<>();

        Stream<LocalDate> weekDates = todaysDate.datesUntil(todaysDate.plusDays(7));
        weekDates.forEach(date -> {
            mealPlans.add(kitchenService.getVotedMealForDate(date,groupId));
        });
        return mealPlans;
    }

    @GetMapping("kitchen/api/member/mealplanstats")
    public Object getMealPlanStats(@RequestParam UUID groupId){
        return null;
    }


}

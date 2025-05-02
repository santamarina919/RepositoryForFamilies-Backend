package dev.J.RepositoryForFamilies.Kitchen;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class KitchenService {

    private final MealRepository mealRepository;

    private final KitchenItemRepository itemRepository;

    public final IngredientRepository ingredientRepository;

    public final DirectionRepository directionRepository;



    public List<FullMealDTO> allMeals(UUID groupId){
        List<MealDetails> groupMeals = mealRepository.fetchAllMeals(groupId);

        List<List<DirectionDetails>> mealDirections = new ArrayList<>();


        List<List<KitchenItem>> allMealIngredients = new ArrayList<>();



        groupMeals.forEach(meal -> {
            List<DirectionDetails> directions  = mealRepository.fetchMealDirections(meal.getName());
            mealDirections.add(directions);

            List<KitchenItem> mealIngredients = mealRepository.fetchIngredients(meal.getName());
            allMealIngredients.add(mealIngredients);


        });

        List<FullMealDTO> mealDtos = new ArrayList<>();
        for(int i = 0; i < groupMeals.size(); i++){
            mealDtos.add(new FullMealDTO());
            FullMealDTO curr = mealDtos.getLast();
            curr.setMealName(groupMeals.get(i).getName());
            curr.setDirections(mealDirections.get(i));
            curr.setIngredients(allMealIngredients.get(i));

        }

        return mealDtos;
    }

    public FullMealDTO getMeal(String mealId){
        Meal m = mealRepository.findById(mealId).orElseThrow(IllegalArgumentException::new);
        List<DirectionDetails> directions = mealRepository.fetchMealDirections(mealId);
        List<KitchenItem> items = mealRepository.fetchIngredients(mealId);

        FullMealDTO mealDTO = new FullMealDTO(m.getName(),m.getTimeToPrepare(),directions,items);
        return mealDTO;
    }

    /**
     * Given a group id and the class of a type it will return all meals according to that type.
     * @param groupId
     * @param clazz
     * @return all meals according to the class description
     * @param <T>
     */
    public <T> List<T> allMeals(UUID groupId, Class<T> clazz){
        return mealRepository.findAllByOwningGroup(groupId,clazz);
    }

    @Transactional
    public void createKitchenItem(String itemName){
        KitchenItem item = new KitchenItem(itemName);
        itemRepository.save(item);
    }

    @Transactional
    public void deleteKitchenItem(String itemName){
        KitchenItem item = new KitchenItem(itemName);
        itemRepository.delete(item);
    }

    @Transactional
    public void createMeal(UUID groupId, String mealName, double timeToPrepare,
                           List<Ingredient.IngredientDetails> ingredients, List<DirectionDetails> directions) {
        mealRepository.createMeal(mealName,groupId,timeToPrepare);
        ingredients
                .forEach(ingredient -> {
                    ingredientRepository.createIngredient(ingredient.getMealName(),ingredient.getItemName(),ingredient.getUnit(),ingredient.getQuantity());
                });
        directions.forEach(direction -> {
            directionRepository.makeDirection(mealName,direction.getStep(),direction.getDirection(),direction.getOptional());
        });

    }

    @Transactional
    public void deleteMeal(String mealName){
        List<Ingredient> ingredients = ingredientRepository.findAllByMealId(mealName);
        List<Direction> directions = directionRepository.findAllByMealId(mealName);

        ingredientRepository.deleteAll(ingredients);
        directionRepository.deleteAll(directions);


    }

    public Integer mealCount(UUID groupId) {
       return mealRepository.fetchMealCount(groupId);
    }

    public List<KitchenItem> allItems(UUID groupId) {
        return itemRepository.findAll();
    }

    public KitchenItem getItem(String itemName) {
        return itemRepository.findById(itemName).orElseThrow(() -> {
            return new IllegalArgumentException("could not find" + itemName);
        });
    }
}

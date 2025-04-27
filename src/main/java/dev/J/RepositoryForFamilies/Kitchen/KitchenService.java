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

    public final SubMealRepository subMealRepository;


    public Collection<FinalMealDTO> allMeals(UUID groupId){
        List<IntermediateMealDTO> intermediateMealDTO = mealRepository.fetchAllMeals(groupId);

       HashMap<String,FinalMealDTO> mealMap = intermediateMealDTO.stream()
                .collect(
                        HashMap::new,
                        (map,dto) -> {
                            map.putIfAbsent(dto.getName(), new FinalMealDTO());
                            FinalMealDTO finalDto = map.get(dto.getName());
                            assert finalDto != null;

                            finalDto.setMealName(dto.getName());
                            finalDto.getDirections().add(dto.getDirection());
                            finalDto.getIngredients().add(dto.getItem());

                        },
                        (map1,map2) -> {
                            map2.entrySet()
                                    .forEach(entry -> {
                                        map1.merge(
                                                entry.getKey(),
                                                entry.getValue(),
                                                (firstEntry,secondEntry) -> {
                                                    firstEntry.getDirections().addAll(secondEntry.getDirections());
                                                    firstEntry.getIngredients().addAll(secondEntry.getIngredients());
                                                    return firstEntry;
                                                }
                                        );
                                    });
                        });


        return mealMap.values();
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
                           List<Ingredient.IngredientDetails> ingredients, List<SubMeal.MealDetails> subMeals, List<DirectionDetails> directions) {
        mealRepository.createMeal(mealName,groupId,timeToPrepare);
        ingredients
                .forEach(ingredient -> {
                    ingredientRepository.createIngredient(ingredient.getMealName(),ingredient.getItemName(),ingredient.getUnit(),ingredient.getQuantity());
                });
        directions.forEach(direction -> {
            directionRepository.makeDirection(mealName,direction.getStep(),direction.getDirection(),direction.isOptional());
        });

        subMeals.forEach(meal -> {
            subMealRepository.createMeal(meal.getPrimaryMeal(),meal.getSubMeal(),meal.isOptional());
        });
    }

    @Transactional
    public void deleteMeal(String mealName){
        List<Ingredient> ingredients = ingredientRepository.findAllByMealId(mealName);
        List<Direction> directions = directionRepository.findAllByMealId(mealName);
        List<SubMeal> subMeals = subMealRepository.findAllByPrimaryMeal(mealName);

        ingredientRepository.deleteAll(ingredients);
        directionRepository.deleteAll(directions);

        subMealRepository.deleteAll(subMeals);

    }
}

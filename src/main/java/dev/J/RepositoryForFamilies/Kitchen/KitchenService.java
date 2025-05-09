package dev.J.RepositoryForFamilies.Kitchen;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class KitchenService {

    private final MealRepository mealRepository;

    private final KitchenItemRepository itemRepository;

    private final IngredientRepository ingredientRepository;

    private final DirectionRepository directionRepository;

    private final MealVoteRepository voteRepository;

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

    //TODO validate
    public boolean voteMeal(String voter, LocalDate date, MealType mealType, String mealId, UUID groupId) {
        //User can only have 1 entry per date and meal type
        Optional<MealVote> vote = voteRepository.findById(new MealVoteId(date,mealType,voter));
        if(vote.isPresent()){
            return false;
        }

        //votes can only be case until the day before.
        LocalDate todayDate = LocalDate.now();
        if(todayDate.isEqual(date) || todayDate.isAfter(date)){
            return false;
        }

        MealVote newVote = new MealVote(date,mealType,mealId,voter, groupId);
        voteRepository.save(newVote);
        return true;
    }

    //TODO return a list with each dates voted meal
    public List<VotedMeal> getVotedMealForDate(LocalDate date, UUID groupId) {
        //voted meals for each meal type
        List<VotedMeal> votedMeals = new ArrayList<>();


        Stream.of(MealType.values())
                .forEach(mealType -> {
                    List<MealVote> votes =  voteRepository.fetchVotesForDay(mealType,date,groupId);
                    HashMap<String,Integer> voteCount = new HashMap<>();
                    votes.forEach(vote -> {
                        Integer previousVal = voteCount.putIfAbsent(vote.getMealId(),1);
                        if(previousVal != null){
                            voteCount.put(vote.getMealId(),voteCount.get(vote.getMealId()) + 1);
                        }
                    });

                    var max = voteCount.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue));
                    if(max.isEmpty()){
                        votedMeals.add(new VotedMeal(date,mealType,null));
                    }

                    votedMeals.add(new VotedMeal(date,mealType,mealRepository.findById(max.get().getKey()).orElseThrow()));
                });
        return votedMeals;
    }

    private String determineMealVoteWinner(List<MealVote> votes) {
        if(votes.isEmpty()){
            return null;
        }
        HashMap<String,List<MealVote>> votesByMealId = new HashMap<>();
        votes.forEach(vote -> {
            votesByMealId.putIfAbsent(vote.getMealId(),new ArrayList<>());
            votesByMealId.get(vote.getMealId())
                    .add(vote);
        });

        int max = 0;
        String votedMeal = null;
        for(var entry : votesByMealId.entrySet()){
            if(entry.getValue().size() > max){
                max = entry.getValue().size();
                votedMeal = entry.getKey();
            }
        }

        return votedMeal;
    }
}

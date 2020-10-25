package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
 //   private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
       // MealsUtil.meals.forEach(this::save);
        MealsUtil.meals.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            meals.put(meal.getId(), meal);
//            repository.put(userId, meals);
            return meal;
        }
        // handle case: update, but not present in storage
        System.out.println(meal);
        if (meals.get(meal.getId()).getUserId() == userId) return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (meals.get(id)!= null && meals.get(id).getUserId() == userId)
                    return meals.remove(id) != null;
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (meals.get(id)!= null && meals.get(id).getUserId() == userId)
                return meals.get(id);
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Comparator<Meal> comparator = Comparator.comparing(Meal::getDate);
//        return repository.get(userId).values().stream().sorted(comparator.reversed()).collect(Collectors.toList());
        return meals.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }
}


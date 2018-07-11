package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        meal.setUserId(userId);
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
        } else {
            repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        log.info("delete meal with id = {}", id);
        Meal result = repository.get(id);
        return (result != null) && Objects.equals(result.getUserId(), userId) && repository.remove(id, result);
    }

    @Override
    public Meal get(int id, Integer userId) {
        log.info("get meal with id = {}", id);
        Meal result = repository.get(id);
        return (result != null && Objects.equals(result.getUserId(), userId)) ? result : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return getAllFilteredByDate(userId, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public List<Meal> getAllFilteredByDate(Integer userId, LocalDate start, LocalDate end) {
        log.info("getAll with filters");
        return repository.values()
                .stream()
                .filter(meal -> Objects.equals(meal.getUserId(), userId))
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), start, end))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}


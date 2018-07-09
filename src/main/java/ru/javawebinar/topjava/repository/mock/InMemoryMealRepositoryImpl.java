package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
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
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return Objects.equals(meal.getUserId(), userId) ?
                repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        if (Objects.equals(repository.get(id).getUserId(), userId))
            return repository.remove(id) != null;
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal result = repository.get(id);
        return Objects.equals(repository.get(id).getUserId(), userId) ? result : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return repository.values()
                .stream()
                .filter(meal -> Objects.equals(meal.getUserId(), userId))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllFilteredByDate(Integer userId, String start, String end) {
        return repository.values()
                .stream()
                .filter(meal -> Objects.equals(meal.getUserId(), userId))
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(),
                        (start == null || start.isEmpty()) ? LocalDate.MIN : LocalDate.parse(start),
                        (end == null || end.isEmpty()) ? LocalDate.MAX : LocalDate.parse(end)))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<MealWithExceed> getAllFilteredByDateTime(Integer userId, int caloriesPerDay, String startDate, String endDate, String startTime, String endTime) {
        LocalTime timeFrom = (startTime == null || startTime.isEmpty()) ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime timeTo = (endTime == null || endTime.isEmpty()) ? LocalTime.MAX : LocalTime.parse(endTime);
        return MealsUtil.getFilteredWithExceeded(getAllFilteredByDate(userId, startDate, endDate), caloriesPerDay, timeFrom, timeTo);
    }
}


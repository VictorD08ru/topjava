package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServiceHardCodeImpl implements MealService {
    private static final Logger log = getLogger(MealServiceHardCodeImpl.class);
    private AtomicInteger count = new AtomicInteger();
    private List<Meal> meals = new CopyOnWriteArrayList<>(fillMeals());

    @Override
    public List<Meal> getMeals() {
        return meals;
    }

    @Override
    public void addMeal(Meal m) {
        if (meals.add(m))
            log.debug("Meal saved successfully, Meal details=" + m);
    }

    @Override
    public void updateMeal(Meal m) {
        AtomicInteger index = new AtomicInteger();
        meals.stream().filter(meal -> meal.getId() == m.getId()).forEach(meal -> index.set(meals.indexOf(meal)));
        Meal meal = meals.set(index.get(), m);
        log.debug("Meal " + meal + " has been updated");
    }

    @Override
    public void removeMeal(int id) {
        if (meals.removeIf(m -> m.getId() == id))
            log.debug("Meal with id=" + id + "removed successfully");
    }

    @Override
    public Meal getMealById(int id) {
        for (Meal meal : meals) {
            if (meal.getId() == id) {
                log.debug(meal.toString());
                return meal;
            }
        }
        log.debug("Meal with id=" + id + "not found");
        return null;
    }

    private List<Meal> fillMeals () {

        return Arrays.asList(
                new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
    }

    @Override
    public void addMeal(LocalDateTime dateTime, String description, int calories) {
        addMeal(new Meal(count.getAndIncrement(), dateTime, description, calories));
    }
}

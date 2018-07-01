package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServiceImpl implements MealService {
    private static final Logger log = getLogger(MealServiceImpl.class);

    private List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2018, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2018, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2018, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2018, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2018, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2018, Month.MAY, 31, 20, 0), "Ужин", 510)
    );

    @Override
    public List<Meal> getMeals() {
        return meals;
    }

    @Override
    public void addMeal(Meal m) {
        meals.add(m);
    }

    @Override
    public void updateMeal(int id, Meal m) {
        meals.set(id, m);
    }

    @Override
    public void removeMeal(Meal m) {
        meals.remove(m);
    }
}

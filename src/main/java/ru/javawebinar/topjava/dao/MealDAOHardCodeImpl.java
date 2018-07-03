package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealDAOHardCodeImpl implements MealDAO {
    private static final Logger log = getLogger(MealDAOHardCodeImpl.class);
    private AtomicInteger count = new AtomicInteger();
    private Map<Integer, Meal> meals = fillMap();

    @Override
    public List<Meal> getMeals() {
        List<Meal> result = new ArrayList<>(meals.values());
        log.debug("get meals");
        return Collections.unmodifiableList(result);
    }

    @Override
    public Meal add(Meal m) {
        if (m.getId() < 0) {
            m = new Meal(count.getAndIncrement(), m.getDateTime(), m.getDescription(), m.getCalories());
        }

        meals.put(m.getId(), m);
        log.debug("Meal saved successfully, Meal details=" + m);
        return m;
    }

    @Override
    public void update(Meal m) {
        if (meals.replace(m.getId(), meals.get(m.getId()), m))
        log.debug("Meal " + m + " has been updated");
    }

    @Override
    public void remove(int id) {
        if (meals.remove(id) != null)
            log.debug("Meal with id=" + id + "removed successfully");
    }

    @Override
    public Meal getById(int id) {
        Meal meal = meals.get(id);
        log.debug("Meal with id=" + id + ": " + meal);
        return meal;
    }

    private Map<Integer, Meal> fillMap() {
        Meal meal1 = new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        Meal meal2 = new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        Meal meal3 = new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        Meal meal4 = new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        Meal meal5 = new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
        Meal meal6 = new Meal(count.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
        Map<Integer, Meal> result = new ConcurrentHashMap<>();
        result.put(meal1.getId(), meal1);
        result.put(meal2.getId(), meal2);
        result.put(meal3.getId(), meal3);
        result.put(meal4.getId(), meal4);
        result.put(meal5.getId(), meal5);
        result.put(meal6.getId(), meal6);

        return result;
    }
}

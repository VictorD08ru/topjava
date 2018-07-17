package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.parse("2018-08-30T14:00"), "Перекус", 800);
        Meal created = service.create(meal, ADMIN_ID);
        List<Meal> expectedMeals = new ArrayList<>(ADMIN_MEALS);
        List<Meal> actualMeals = service.getAll(ADMIN_ID);
        meal.setId(created.getId());
        expectedMeals.add(meal);
        expectedMeals.sort(Comparator.comparing(Meal::getDateTime).reversed());
        assertMatch(actualMeals, expectedMeals);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeCreate() throws Exception {
        Meal meal = new Meal(LocalDateTime.parse("2018-07-30T14:00"), "Pizza Time", 1700);
        service.create(meal, USER_ID);
    }

    @Test
    public void get() {
        Meal actualMeal = service.get(100003, USER_ID);
        Meal expectedMeal = USER_MEALS.stream()
                                .filter(m -> m.getId() == 100003)
                                .findFirst().orElse(null);
        //for fail test
//        expectedMeal.setId(1);
//        expectedMeal.setDateTime(LocalDateTime.now());
//        expectedMeal.setDescription("kuku");
//        expectedMeal.setCalories(20);
        assertMatch(actualMeal, expectedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(100003, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(100010, USER_ID);
        List<Meal> expectedMeals = new ArrayList<>(USER_MEALS);
        List<Meal> actualMeals = service.getAll(USER_ID);
        expectedMeals.removeIf(m -> m.getId() == 100010);
        assertMatch(actualMeals, expectedMeals);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(100011, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> actualMeals = service
                .getBetweenDates(
                        LocalDate.parse("2018-07-29"),
                        LocalDate.parse("2018-07-30"), USER_ID);
        List<Meal> expectedMeals = USER_MEALS.subList(3, USER_MEALS.size());
        assertMatch(actualMeals, expectedMeals);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> actualMeals = service
                .getBetweenDateTimes(
                        LocalDateTime.parse("2018-07-29T09:00"),
                        LocalDateTime.parse("2018-07-30T14:00"), USER_ID);
        List<Meal> expectedMeals = USER_MEALS.subList(4, USER_MEALS.size());
        assertMatch(actualMeals, expectedMeals);
    }

    @Test
    public void getAll() {
        List<Meal> list = service.getAll(ADMIN_ID);
//        Assertions.assertThat(list).isEqualTo(ADMIN_MEALS);
        assertEquals(ADMIN_MEALS, list);
    }

    @Test
    public void update() {
        Meal updated = ADMIN_MEALS.stream()
                .filter(m -> m.getId() == 100012)
                .findFirst().orElse(null);
        assert updated != null;
        updated.setDateTime(LocalDateTime.now());
        updated.setCalories(1200);
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(updated.getId(), ADMIN_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(new Meal(1, LocalDateTime.now(), "sd", 100), ADMIN_ID);
    }
}
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
import java.util.List;

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
        Meal meal = new Meal(LocalDateTime.parse("2018-07-30T13:00"), "Перекус", 800);
        Meal created = service.create(meal, ADMIN_ID);
        List<Meal> actualMeals = service.getAll(ADMIN_ID);
        meal.setId(created.getId());
        assertMatch(actualMeals, ADMIN_MEAL13, ADMIN_MEAL12, ADMIN_MEAL11, meal);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeCreate() throws Exception {
        Meal meal = new Meal(LocalDateTime.parse("2018-07-30T13:00"), "Pizza Time", 1700);
        service.create(meal, USER_ID);
    }

    @Test
    public void get() {
        Meal actualMeal = service.get(ID03_FOR_USER, USER_ID);
        assertMatch(actualMeal, USER_MEAL03);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(ID03_FOR_USER, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(ID10_FOR_USER, USER_ID);
        List<Meal> actualMeals = service.getAll(USER_ID);
        assertMatch(actualMeals, USER_MEAL09, USER_MEAL08, USER_MEAL07, USER_MEAL06,
                USER_MEAL05, USER_MEAL04, USER_MEAL03, USER_MEAL02);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(ID11_FOR_ADMIN, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> actualMeals = service
                .getBetweenDates(
                        LocalDate.parse("2018-07-29"),
                        LocalDate.parse("2018-07-30"), USER_ID);
        assertMatch(actualMeals, USER_MEAL07, USER_MEAL06, USER_MEAL05, USER_MEAL04,
                USER_MEAL03, USER_MEAL02);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> actualMeals = service
                .getBetweenDateTimes(
                        LocalDateTime.parse("2018-07-29T09:00"),
                        LocalDateTime.parse("2018-07-30T14:00"), USER_ID);
        assertMatch(actualMeals, USER_MEAL06, USER_MEAL05, USER_MEAL04, USER_MEAL03,
                USER_MEAL02);
    }

    @Test
    public void getAll() {
        List<Meal> list = service.getAll(ADMIN_ID);
        assertMatch(list, ADMIN_MEAL13, ADMIN_MEAL12, ADMIN_MEAL11);
    }

    @Test
    public void update() {
        Meal updated = new Meal(ADMIN_MEAL12);
        updated.setDateTime(LocalDateTime.now());
        updated.setCalories(1200);
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(updated.getId(), ADMIN_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(new Meal(1, LocalDateTime.now(), "sd", 100), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateFromWrongUser() {
        Meal updated = new Meal(ADMIN_MEAL12);
        updated.setDateTime(LocalDateTime.now());
        updated.setCalories(1200);
        service.update(updated, USER_ID);
    }
}
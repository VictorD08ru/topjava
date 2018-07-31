package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    @Transactional
    public void getMealsByUserId() {
        List<Meal> list = service.get(UserTestData.USER_ID).getMeals();
        Meal meal = null;
        if (!list.isEmpty()) {
            meal = list.get(list.size() - 1);
        }
        assertMatch(meal, MEAL1);
    }
}

package ru.javawebinar.topjava.service.datajpa;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.repository.datajpa.CrudMealRepository;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Autowired
    private CrudMealRepository crudMealRepository;

    @Test
    @Transactional
    public void getUserByMealId() {
        int userId = crudMealRepository.getOne(MealTestData.MEAL1_ID).getUser().getId();
        Assertions.assertThat(userId).isEqualTo(UserTestData.USER_ID);
    }
}

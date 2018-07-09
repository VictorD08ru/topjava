package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, Integer userId) {
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, Integer userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, Integer userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void update(Meal meal, Integer userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Override
    public List<MealWithExceed> getAll(Integer userId, int caloriesPerDay) {
        return MealsUtil.getWithExceeded(repository.getAll(userId), caloriesPerDay);
    }

    @Override
    public List<MealWithExceed> getAllFiltered(Integer userId, int caloriesPerDay, String startDate, String endDate, String startTime, String endTime) {
        LocalTime timeFrom = (startTime == null || startTime.isEmpty()) ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime timeTo = (endTime == null || endTime.isEmpty()) ? LocalTime.MAX : LocalTime.parse(endTime);
        return MealsUtil.getFilteredWithExceeded(repository.getAllFilteredByDate(userId, startDate, endDate), caloriesPerDay, timeFrom, timeTo);
    }
}
package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    List<Meal> getMeals();
    void addMeal(Meal m);
    void addMeal(LocalDateTime dateTime, String description, int calories);
    void updateMeal(Meal m);
    void removeMeal(int id);
    Meal getMealById(int id);
}

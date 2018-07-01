package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    List<Meal> getMeals();
    void addMeal(Meal m);
    void updateMeal(int id, Meal m);
    void removeMeal(Meal m);
}

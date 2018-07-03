package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDAO {
    List<Meal> getMeals();
    Meal add(Meal m);
    void update(Meal m);
    void remove(int id);
    Meal getById(int id);
}

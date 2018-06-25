package ru.javawebinar.topjava.util;

import java.util.*;

/*
* Auxiliary package-private class
* @see ru.javawebinar.topjava.util.UserMealsUtil
* */
class CalculateCaloriesHelper {
    private LinkedList<Integer> indexes;

    private int sum = 0;

    CalculateCaloriesHelper() {
        indexes = new LinkedList<>();
    }

    boolean hasIndexes() {
        return !indexes.isEmpty();
    }

    void addIndex(Integer index) {
        indexes.add(index);
    }

    int poll() {
        Integer result = indexes.poll();
        return result != null ? result : -1;
    }

    void increment(int calories) {
        this.sum += calories;
    }

    int getSum() {
        return this.sum;
    }
}

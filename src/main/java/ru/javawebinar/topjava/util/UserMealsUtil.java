package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return doHomework0WithLoop(mealList, startTime, endTime, caloriesPerDay);
//        return doHomework0WithStream(mealList, startTime, endTime, caloriesPerDay);
    }

    /*
     * Итоговая временная сложность метода - O(2N).
     * */
    private static List<UserMealWithExceed> doHomework0WithStream (List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();

        mealList.forEach(userMeal -> caloriesPerDayMap.merge(
                userMeal.getDateTime().toLocalDate(),
                userMeal.getCalories(),
                Integer::sum
        ));

        return mealList.stream()
                .filter(u1 -> TimeUtil.isBetween(u1.getDateTime().toLocalTime(), startTime, endTime))
                .map(u2 -> new UserMealWithExceed(
                        u2.getDateTime(),
                        u2.getDescription(),
                        u2.getCalories(),
                        caloriesPerDayMap.get(u2.getDateTime().toLocalDate()) > caloriesPerDay
                ))
                .collect(Collectors.toList());
    }

    /*
    * В худшем случае временная сложность метода - O(2N)
    *
    * */
    private static List<UserMealWithExceed> doHomework0WithLoop(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> result = new ArrayList<>();
        Map<LocalDate, CalculateCaloriesHelper> caloriesPerDayMap = new HashMap<>();

        for (UserMeal userMeal : mealList) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            int calories = userMeal.getCalories();

            if (caloriesPerDayMap.isEmpty() || !caloriesPerDayMap.containsKey(localDate)) {
                caloriesPerDayMap.put(localDate, new CalculateCaloriesHelper());
            }

            CalculateCaloriesHelper helper = caloriesPerDayMap.get(localDate);
            helper.increment(calories);
            boolean exceeded = helper.getSum() > caloriesPerDay;

            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExceed(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        exceeded));
                if (!exceeded) {
                    helper.addIndex(result.size() - 1);
                }
            }

            if (exceeded) {
                while (helper.hasIndexes()) {
                    int index = helper.poll();
                    UserMealWithExceed userMealWithExceed = result.get(index);
                    result.set(index, new UserMealWithExceed(
                            userMealWithExceed.getDateTime(),
                            userMealWithExceed.getDescription(),
                            userMealWithExceed.getCalories(),
                            true
                    ));
                }
            }
        }

        return result;
    }
}

package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    /*
    * Итоговая временная сложность метода O(N*log(N) + N*2) - сортировка + 2 прохода по списку
    * */
    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        ArrayList<UserMeal> sortedMealList = new ArrayList<>(mealList);
        List<UserMealWithExceed> result = new ArrayList<>();
        sortedMealList.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
        LocalDate oneDay = sortedMealList.get(0).getDateTime().toLocalDate();

        for (int i = 0, calSumPerDay = 0; i < sortedMealList.size(); i++) {

            //вычисление количества калорий за день
            if (sortedMealList.get(i)
                    .getDateTime()
                    .toLocalDate()
                    .equals(oneDay))
                calSumPerDay += sortedMealList.get(i).getCalories();

            if ((!sortedMealList.get(i)
                    .getDateTime()
                    .toLocalDate()
                    .equals(oneDay) ||
                    i == sortedMealList.size() - 1)) {
                //в зависимости от того, этот элемент последний или нет
                //меняется значение первой даты в валидации цикла for
                int buffer = (i == sortedMealList.size() - 1) ? i : i - 1;
                //проходим по всем трапезам этого дня от последней по времени к первой
                for (int j = buffer; j >= 0 &&
                        sortedMealList.get(j)
                                .getDateTime()
                                .toLocalDate()
                                .equals(oneDay); j--) {
                    if (TimeUtil.isBetween(
                            sortedMealList.get(j)
                                    .getDateTime()
                                    .toLocalTime(),
                            startTime, endTime))
                        result.add(new UserMealWithExceed(
                                sortedMealList.get(j).getDateTime(),
                                sortedMealList.get(j).getDescription(),
                                sortedMealList.get(j).getCalories(),
                                calSumPerDay > caloriesPerDay));

                }

                calSumPerDay = sortedMealList.get(i).getCalories();
                oneDay = sortedMealList.get(i).getDateTime().toLocalDate();
            }

        }

        return result;
    }
}

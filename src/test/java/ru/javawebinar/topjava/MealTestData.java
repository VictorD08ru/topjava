package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final int ID02_FOR_USER = 100002;
    public static final int ID03_FOR_USER = 100003;
    public static final int ID04_FOR_USER = 100004;
    public static final int ID05_FOR_USER = 100005;
    public static final int ID06_FOR_USER = 100006;
    public static final int ID07_FOR_USER = 100007;
    public static final int ID08_FOR_USER = 100008;
    public static final int ID09_FOR_USER = 100009;
    public static final int ID10_FOR_USER = 100010;
    public static final int ID11_FOR_ADMIN = 100011;
    public static final int ID12_FOR_ADMIN = 100012;
    public static final int ID13_FOR_ADMIN = 100013;
    public static final Meal USER_MEAL02 = new Meal(ID02_FOR_USER, LocalDateTime.parse("2018-07-29T10:00"), "Завтрак", 500);
    public static final Meal USER_MEAL03 = new Meal(ID03_FOR_USER, LocalDateTime.parse("2018-07-29T13:00"), "Обед", 1000);
    public static final Meal USER_MEAL04 = new Meal(ID04_FOR_USER, LocalDateTime.parse("2018-07-29T20:00"), "Ужин", 500);
    public static final Meal USER_MEAL05 = new Meal(ID05_FOR_USER, LocalDateTime.parse("2018-07-30T10:00"), "Завтрак", 500);
    public static final Meal USER_MEAL06 = new Meal(ID06_FOR_USER, LocalDateTime.parse("2018-07-30T13:00"), "Обед", 1000);
    public static final Meal USER_MEAL07 = new Meal(ID07_FOR_USER, LocalDateTime.parse("2018-07-30T20:00"), "Ужин", 500);
    public static final Meal USER_MEAL08 = new Meal(ID08_FOR_USER, LocalDateTime.parse("2018-07-31T10:00"), "Завтрак", 500);
    public static final Meal USER_MEAL09 = new Meal(ID09_FOR_USER, LocalDateTime.parse("2018-07-31T13:00"), "Обед", 1000);
    public static final Meal USER_MEAL10 = new Meal(ID10_FOR_USER, LocalDateTime.parse("2018-07-31T20:00"), "Ужин", 510);
    public static final Meal ADMIN_MEAL11 = new Meal(ID11_FOR_ADMIN, LocalDateTime.parse("2018-08-01T07:00"), "Завтрак админа", 200);
    public static final Meal ADMIN_MEAL12 = new Meal(ID12_FOR_ADMIN, LocalDateTime.parse("2018-08-01T14:00"), "Ланч админа", 1510);
    public static final Meal ADMIN_MEAL13 = new Meal(ID13_FOR_ADMIN, LocalDateTime.parse("2018-08-01T21:00"), "Ужин админа", 510);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }
}

package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.*;

public class MealTestData {

//            ('2018-07-29T10:00'::timestamp, 'Завтрак', 500, 100000 ),
//            ('2018-07-29T13:00'::timestamp, 'Обед', 1000, 100000 ),
//            ('2018-07-29T20:00'::timestamp, 'Ужин', 500, 100000 ),
//            ('2018-07-30T10:00'::timestamp, 'Завтрак', 500, 100000 ),
//            ('2018-07-30T13:00'::timestamp, 'Обед', 1000, 100000 ),
//            ('2018-07-30T20:00'::timestamp, 'Ужин', 500, 100000 ),
//            ('2018-07-31T10:00'::timestamp, 'Завтрак', 500, 100000 ),
//            ('2018-07-31T13:00'::timestamp, 'Обед', 1000, 100000 ),
//            ('2018-07-31T20:00'::timestamp, 'Ужин', 510, 100000 ),
//            ('2018-07-01T07:00'::timestamp, 'Завтрак админа', 200, 100001 ),
//            ('2018-07-01T14:00'::timestamp, 'Ланч админа', 1510, 100001 ),
//            ('2018-07-01T21:00'::timestamp, 'Ужин админа', 510, 100001 );
    private static int count = START_SEQ + 1;
    public final static List<Meal> USER_MEALS = Arrays.asList(
            new Meal(++count, LocalDateTime.parse("2018-07-29T10:00"), "Завтрак", 500),
            new Meal(++count, LocalDateTime.parse("2018-07-29T13:00"), "Обед", 1000),
            new Meal(++count, LocalDateTime.parse("2018-07-29T20:00"), "Ужин", 500),
            new Meal(++count, LocalDateTime.parse("2018-07-30T10:00"), "Завтрак", 500),
            new Meal(++count, LocalDateTime.parse("2018-07-30T13:00"), "Обед", 1000),
            new Meal(++count, LocalDateTime.parse("2018-07-30T20:00"), "Ужин", 500),
            new Meal(++count, LocalDateTime.parse("2018-07-31T10:00"), "Завтрак", 500),
            new Meal(++count, LocalDateTime.parse("2018-07-31T13:00"), "Обед", 1000),
            new Meal(++count, LocalDateTime.parse("2018-07-31T20:00"), "Ужин", 510)
    );
    public final static List<Meal> ADMIN_MEALS = Arrays.asList(
            new Meal(++count, LocalDateTime.parse("2018-08-01T07:00"), "Завтрак админа", 200),
            new Meal(++count, LocalDateTime.parse("2018-08-01T14:00"), "Ланч админа", 1510),
            new Meal(++count, LocalDateTime.parse("2018-08-01T21:00"), "Ужин админа", 510)
    );

    static
    {
        USER_MEALS.sort(Comparator.comparing(Meal::getDateTime).reversed());
        ADMIN_MEALS.sort(Comparator.comparing(Meal::getDateTime).reversed());
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual)
                .usingElementComparator(
                        Comparator.comparing(Meal::getId)
                        .thenComparing(Meal::getDateTime)
                        .thenComparing(Meal::getDescription)
                        .thenComparing(Meal::getCalories)).isEqualTo(expected);
    }
}

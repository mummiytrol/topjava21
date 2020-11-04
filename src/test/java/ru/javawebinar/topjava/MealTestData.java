package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int NOT_FOUND = 10;
    public static final int MEAL_ID = START_SEQ + 2;
    public static final Meal MEAL = new Meal(null, LocalDateTime.of(2020, Month.OCTOBER, 30, 10, 0),
            "Завтрак", 500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.SEPTEMBER, 30, 10, 0),
                "Завтрак", 1000);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id", "user_id").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("id", "user_id").isEqualTo(expected);
    }

}

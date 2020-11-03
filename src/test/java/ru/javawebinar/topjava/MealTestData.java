package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;
    public static final int MEAL_ID = START_SEQ + 2;
    public static final Meal MEAL = new Meal(null, LocalDateTime.of(2020, Month.OCTOBER, 30, 10, 0),
            "Завтрак", 500);
    public static final LocalDateTime DATE_TIME = LocalDateTime.of(2020, Month.MAY, 19, 10, 0);
    public static Meal getNew() {

        return new Meal(null, LocalDateTime.of(2020, Month.SEPTEMBER, 30, 10, 0),
                "Завтрак", 1000);
    }
    public static Meal getUpdated() {
        Meal updated = MEAL;
        updated.setDescription("UpdatedName");
        updated.setDateTime(DATE_TIME);
        updated.setCalories(500);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id", "user_id").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("id", "user_id").isEqualTo(expected);
    }

}

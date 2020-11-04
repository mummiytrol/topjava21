package ru.javawebinar.topjava.service;

import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MealServiceTest extends TestCase {
    private final static User testUser= UserTestData.user;
    private static Meal newMeal;
    private static Meal created;

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @BeforeClass
    public static void init() {
        newMeal = MealTestData.getNew();

    }

    @AfterClass
    public static void destroy() {
        newMeal = null;
    }

    @Test
    public void create() {
        created = service.create(newMeal, testUser.getId());
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(created.getId(), testUser.getId()), newMeal);
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, testUser.getId());
        MealTestData.assertMatch(meal, meal);
    }

    @Test
    public void getAlien() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, NOT_FOUND));

    }

    @Test
    public void deleteAlien() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, NOT_FOUND));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, testUser.getId());
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, testUser.getId()));
    }

    public void testGetBetweenInclusive() {

    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(UserTestData.user.getId());
        MealTestData.assertMatch(all, MealsUtil.meals.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()));
    }

    @Test
    public void update() {
        MEAL.setId(MEAL_ID);
        MEAL.setCalories(400);
        MEAL.setDescription("Перекус");
        service.update(MEAL, testUser.getId());
        MealTestData.assertMatch(service.get(MEAL_ID, testUser.getId()), MEAL);
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(created, NOT_FOUND));
    }

    @Test
    public void duplicatedDateTimeCreate() {
        assertThrows(DataAccessException.class, () -> service.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                "Ланч", 500), testUser.getId()));
    }
}
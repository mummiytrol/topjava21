package ru.javawebinar.topjava.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL;
import static ru.javawebinar.topjava.MealTestData.MEAL_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest extends TestCase {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void testCreate() {
        User testUser = UserTestData.user;
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, testUser.getId());
        int newId = created.getId();

        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, testUser.getId()), newMeal);
    }

    @Test
    public void testGet() {
        User testUser = UserTestData.user;
        Meal meal = service.get(MEAL_ID, testUser.getId());
        MealTestData.assertMatch(meal, meal);
    }

    @Test
    public void testDelete() {
        User testUser = UserTestData.user;
        service.delete(MEAL_ID, testUser.getId());
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, testUser.getId()));
    }

    public void testGetBetweenInclusive() {

    }

    @Test
    public void testGetAll() {
        List<Meal> all = service.getAll(UserTestData.user.getId());
        MEAL.setId(MEAL_ID);
        MealTestData.assertMatch(all, MEAL);
    }

    @Test
    public void testUpdate() {
        User testUser = UserTestData.user;
        MEAL.setId(MEAL_ID);
        MEAL.setCalories(400);
        MEAL.setDescription("Перекус");
        service.update(MEAL, testUser.getId());
        MealTestData.assertMatch(service.get(MEAL_ID, testUser.getId()), MEAL);
    }


}
package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static int id = 10;
    List<Meal> meals = new ArrayList<>();
    {
        meals.addAll(MealsUtil.MEALS);
    }
    List<MealTo> mealTos;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals. Method GET");

//        request.getRequestDispatcher("/users.jsp").forward(request, response);
        mealTos = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(23, 0), MealsUtil.DEFAULT_CALORIES_PER_DAY);
        request.setAttribute("meals", mealTos);
        request.getRequestDispatcher("meals.jsp").forward(request, response);

 //       response.sendRedirect("meals.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Method POST");
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        LocalDateTime dt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        try {
            dt = LocalDateTime.parse(req.getParameter("dateTime"),formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Meal meal = new Meal();
        meal.setId(id++);
        meal.setCalories(calories);
        meal.setDateTime(dt);
        meal.setDescription(description);
        meals.add(meal);
        mealTos = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(23, 0), MealsUtil.DEFAULT_CALORIES_PER_DAY);
     //   resp.getWriter().write(dt.toString() + " " + description +" " + calories);
                req.setAttribute("meals", mealTos);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);

    }
}

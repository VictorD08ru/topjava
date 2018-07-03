package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceHardCodeImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_OF_MEALS = "/meals.jsp";

    private MealService mealService;

    public MealServlet() {
        super();
        mealService = new MealServiceHardCodeImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("meals:get");
        String action = req.getParameter("action");
        String forward;

        if ("delete".equalsIgnoreCase(action)) {
            int id = Integer.valueOf(req.getParameter("mealId"));
            mealService.removeMeal(id);
            forward = LIST_OF_MEALS;
            req.setAttribute("meals", MealsUtil.getFilteredWithExceeded(mealService.getMeals(), LocalTime.MIN,
                    LocalTime.MAX, 2000));
        } else if ("edit".equalsIgnoreCase(action)) {
            forward = INSERT_OR_EDIT;
            int id = Integer.valueOf(req.getParameter("mealId"));
            Meal meal = mealService.getMealById(id);
            req.setAttribute("mealForEdit", meal);
        } else if("insert".equalsIgnoreCase(action)) {
            forward = INSERT_OR_EDIT;
        } else {
            forward = LIST_OF_MEALS;
            req.setAttribute("meals", MealsUtil.getFilteredWithExceeded(mealService.getMeals(), LocalTime.MIN,
                    LocalTime.MAX, 2000));
        }

        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("meals:post");
        req.setCharacterEncoding("UTF-8");
        String mealId = req.getParameter("mealId");
        String dateTime = req.getParameter("dateTime");
        String description = req.getParameter("description");
        String calories = req.getParameter("calories");

        if (mealId == null || mealId.isEmpty()) {
            mealService.addMeal(
                    LocalDateTime.parse(dateTime),
                    description,
                    Integer.valueOf(calories));
        } else  {
            int id = Integer.valueOf(mealId);
            mealService.updateMeal(new Meal(
                    id,
                    LocalDateTime.parse(dateTime),
                    description,
                    Integer.valueOf(calories)));
        }
        req.setAttribute("meals", MealsUtil.getFilteredWithExceeded(mealService.getMeals(), LocalTime.MIN,
                LocalTime.MAX, 2000));
        req.getRequestDispatcher(LIST_OF_MEALS).forward(req, resp);
    }
}

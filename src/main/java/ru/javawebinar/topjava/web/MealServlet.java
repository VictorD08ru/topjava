package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDAOHardCodeImpl;
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
    private static final String LIST_OF_MEALS = "meals.jsp";

    private MealDAO mealDAO;

    public MealServlet() {
    }

    @Override
    public void init() {
        mealDAO = new MealDAOHardCodeImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("meals:get");
        String action = req.getParameter("action");
        if (action == null) action = "k";

        switch (action) {
            case "edit":
                int id = Integer.valueOf(req.getParameter("mealId"));
                req.setAttribute("mealForEdit", mealDAO.getById(id));
            case "insert":
                req.getRequestDispatcher(INSERT_OR_EDIT).forward(req, resp);
                break;
            case "delete":
                id = Integer.valueOf(req.getParameter("mealId"));
                mealDAO.remove(id);
                resp.sendRedirect(req.getContextPath() + req.getServletPath());
                break;
            default:
                req.setAttribute("meals", MealsUtil.getFilteredWithExceeded(mealDAO.getMeals(), LocalTime.MIN,
                        LocalTime.MAX, 2000));
                req.getRequestDispatcher(LIST_OF_MEALS).forward(req, resp);
                break;
        }

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
            mealDAO.add(new Meal(
                    LocalDateTime.parse(dateTime),
                    description,
                    Integer.valueOf(calories)));
        } else  {
            int id = Integer.valueOf(mealId);
            mealDAO.update(new Meal(
                    id,
                    LocalDateTime.parse(dateTime),
                    description,
                    Integer.valueOf(calories)));
        }
        req.setAttribute("meals", MealsUtil.getFilteredWithExceeded(mealDAO.getMeals(), LocalTime.MIN,
                LocalTime.MAX, 2000));
        req.getRequestDispatcher(LIST_OF_MEALS).forward(req, resp);
    }
}

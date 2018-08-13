package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@RequestMapping("/meals")
@Controller
public class JspMealController extends AbstractMealController {

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        delete(getId(request));
        return "redirect:/meals";
    }

    @GetMapping("/save")
    public String load(HttpServletRequest request) {
        String id = request.getParameter("id");
        Meal meal = (id == null || id.isEmpty()) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                get(getId(request));
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/save")
    public String save(HttpServletRequest request) {
        String id = request.getParameter("id");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if ((id == null || id.isEmpty())) {
            create(meal);
        } else {
            update(meal, getId(request));
        }
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}

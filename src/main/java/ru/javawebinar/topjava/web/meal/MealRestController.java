package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal);
    }

    public List<MealWithExceed> getFiltered(String startDate, String endDate, String startTime, String endTime) {
        List<MealWithExceed> result = filterByDate(getAll(), startDate, endDate);
        return filterByTime(result, startTime, endTime);
    }

    private List<MealWithExceed> filterByDate(List<MealWithExceed> meals, String startDate, String endDate) {
        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(),
                        (startDate == null || startDate.isEmpty()) ? LocalDate.MIN : LocalDate.parse(startDate),
                        (endDate == null || endDate.isEmpty()) ? LocalDate.MAX : LocalDate.parse(endDate)))
                .collect(Collectors.toList());
    }

    private List<MealWithExceed> filterByTime(List<MealWithExceed> meals, String startTime, String endTime) {
        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(),
                        (startTime == null || startTime.isEmpty()) ? LocalTime.MIN : LocalTime.parse(startTime),
                        (endTime == null || endTime.isEmpty()) ? LocalTime.MAX : LocalTime.parse(endTime)))
                .collect(Collectors.toList());
    }
}
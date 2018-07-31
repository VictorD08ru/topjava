package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Override
    Meal save(Meal meal);

    @Transactional
    @Modifying
    @Query("UPDATE Meal m SET m.description = :description, m.dateTime = :dateTime, " +
            "m.calories = :calories WHERE m.id = :id AND m.user.id = :userId")
    int updateByQuery(@Param("id") int id,
               @Param("description") String description,
               @Param("dateTime") LocalDateTime dateTime,
               @Param("calories") int calories,
               @Param("userId") int userId);

    default Meal update(Meal meal, int userId) {
        return updateByQuery(meal.getId(), meal.getDescription(),
                meal.getDateTime(), meal.getCalories(), userId) != 0 ? meal : null;
    }

    Optional<Meal> findByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserId(int userId, Sort sort);

    List<Meal> findAllByDateTimeBetweenAndUserId(LocalDateTime start, LocalDateTime end, int userId, Sort sort);
}

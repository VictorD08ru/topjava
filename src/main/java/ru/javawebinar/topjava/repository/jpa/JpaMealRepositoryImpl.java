package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = em.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(user);
            em.persist(meal);
            return meal;
        } else {
            if (em.createQuery("UPDATE Meal m " +
                    "SET m.description=?1, m.calories=?2, m.dateTime=?3 " +
                    " WHERE m.id=?4 AND m.user.id=?5")
                    .setParameter(1, meal.getDescription())
                    .setParameter(2, meal.getCalories())
                    .setParameter(3, meal.getDateTime())
                    .setParameter(4, meal.getId())
                    .setParameter(5, userId)
                    .executeUpdate() == 0) {
                return null;
            }
            return meal;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        if (em.getReference(User.class, userId) == null) {
            return false;
        }
        Query query = em.createQuery("DELETE FROM Meal m WHERE m.id=?1 AND m.user.id=?2")
                .setParameter(1, id)
                .setParameter(2, userId);
        return query.setParameter(1, id).executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        em.getReference(User.class, userId);
        List<Meal> list = em.createQuery(
                    "SELECT m FROM Meal m WHERE m.id = ?1 AND m.user.id = ?2", Meal.class)
                    .setParameter(1, id)
                    .setParameter(2, userId)
                    .getResultList();
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public List<Meal> getAll(int userId) {
        em.getReference(User.class, userId);
        return em.createQuery(
                "SELECT m FROM Meal m WHERE m.user.id=?1 ORDER BY m.dateTime DESC", Meal.class)
                .setParameter(1, userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        em.getReference(User.class, userId);
        return em.createQuery(
                "SELECT m FROM Meal m WHERE m.user.id=?1 AND m.dateTime BETWEEN ?2 AND ?3 ORDER BY m.dateTime DESC", Meal.class)
                .setParameter(1, userId)
                .setParameter(2, startDate)
                .setParameter(3, endDate)
                .getResultList();
    }
}
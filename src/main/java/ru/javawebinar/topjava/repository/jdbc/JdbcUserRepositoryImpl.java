package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final ResultSetExtractor<List<User>> extractor = rs -> {
        Map<Integer, User> map = new HashMap<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String password = rs.getString("password");
            Date registered = rs.getTimestamp("registered");
            boolean enabled = rs.getBoolean("enabled");
            int caloriesPerDay = rs.getInt("calories_per_day");
            Role role = Role.valueOf(rs.getString("role"));

            map.compute(id, (key, val) -> {
                Set<Role> roles = (val == null) ? EnumSet.noneOf(Role.class) : val.getRoles();
                if (roles.add(role)) {
                    return new User(id, name, email, password, caloriesPerDay, enabled, registered, roles);
                } else {
                    return val;
                }
            });


        }
        List<User> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparing(User::getName).thenComparing(User::getEmail));
        return list;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        String insertRole = "INSERT INTO user_roles (role, user_id) VALUES (?, ?)";

        List<Role> roles = new ArrayList<>(user.getRoles());
        List<String> updatedUsersRoles;

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }

        updatedUsersRoles = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", String.class, user.getId());
        if (!roles.isEmpty() && updatedUsersRoles != null && !updatedUsersRoles.isEmpty()) {
            updatedUsersRoles.stream().<Predicate<? super Role>>map(s -> r -> r == Role.valueOf(s)).forEach(roles::removeIf);
        }
        if (!roles.isEmpty()) {
            jdbcTemplate.batchUpdate(insertRole, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, roles.get(i).toString());
                    ps.setInt(2, user.getId());
                }

                @Override
                public int getBatchSize() {
                    return roles.size();
                }
            });
        }

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE u.id=?", extractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE u.email=?", extractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id", extractor);
    }
}

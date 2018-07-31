package ru.javawebinar.topjava.util.converter;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.util.converter.DateTimeConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@Profile(Profiles.HSQL_DB)
public class DateTimeConverterForHsqldb implements DateTimeConverter {
    @Override
    public Timestamp convert(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}

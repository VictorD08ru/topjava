package ru.javawebinar.topjava.util.converter;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.Profiles;

import java.time.LocalDateTime;

@Component
@Profile("!" + Profiles.HSQL_DB)
public class DateTimeConverterImpl implements DateTimeConverter {

    @Override
    public LocalDateTime convert(LocalDateTime dateTime) {
        return dateTime;
    }
}

package ru.javawebinar.topjava.util.converter;

import java.time.LocalDateTime;

public interface DateTimeConverter {

    <T> T convert(LocalDateTime dateTime);
}

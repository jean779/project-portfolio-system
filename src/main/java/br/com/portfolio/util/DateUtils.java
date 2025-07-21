package br.com.portfolio.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final String DEFAULT_PATTERN = "dd/MM/yyyy";

    public static String format(LocalDate date) {
        return format(date, DEFAULT_PATTERN);
    }

    public static String format(LocalDate date, String pattern) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
}

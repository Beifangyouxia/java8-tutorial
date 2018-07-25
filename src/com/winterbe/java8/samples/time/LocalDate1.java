package com.winterbe.java8.samples.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * @author Benjamin Winterberg
 */
public class LocalDate1 {

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        LocalDate yesterday = tomorrow.minusDays(2);

        System.out.println("today=" + today);
        System.out.println("tomorrow=" + tomorrow);
        System.out.println("yesterday=" + yesterday);

        LocalDate independenceDay = LocalDate.of(2018, Month.JULY, 25);
        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
        System.out.println("dayOfWeek=" + dayOfWeek); // FRIDAY

        DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);

        LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
        System.out.println(xmas); // 2014-12-24

    }

}

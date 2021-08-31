package net.request;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Query {

    public static String getQueryName() {
        return QUERY_NAME;
    }

    public static String getQueryValue() {
        return String.format(QUERY_VALUE, getStartOfTheWeek());
    }

    private static String getStartOfTheWeek() {
        var mondayDate = LocalDate.now().with(DayOfWeek.MONDAY);
        mondayDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return mondayDate.toString();
    }

    private static final String QUERY_NAME = "jql";
    private static final String QUERY_VALUE = "issuetype in (Bug, Documentation, Enhancement) AND updated >= %s";
}

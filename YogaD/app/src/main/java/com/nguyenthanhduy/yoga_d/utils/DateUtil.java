package com.nguyenthanhduy.yoga_d.utils;

import java.util.Calendar;

public class DateUtil {
    public static boolean isValidDateOfDayOfWeek(Calendar calendar, int dayOfWeek) {
        return dayOfWeek == calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String dayOfWeekToString(int dayOfWeek) throws Exception {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "invalid date";
        }
    }
}

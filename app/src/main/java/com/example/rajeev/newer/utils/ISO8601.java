package com.example.rajeev.newer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Helper class for handling a most common subset of ISO 8601 strings
 * (in the following format: "2008-03-01T13:00:00+01:00"). It supports
 * parsing the "Z" timezone, but many other less-used features are
 * missing.
 */
public final class ISO8601 {

    private static final String LOG_TAG = ISO8601.class.getName();

    /** Transform Calendar to ISO 8601 string. */
    public static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /** Get current date and time formatted as ISO 8601 string. */
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }

    /** Transform ISO 8601 string to Calendar. */
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = GregorianCalendar.getInstance(timeZone);
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }

    public static long[] getTimeElapsedArray(Date publishedDate){
        long [] elapsedTimeArray = new long[3];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL, yyyy h:mm a");
        Date current = Calendar.getInstance().getTime();
        long difference = current.getTime() - publishedDate.getTime();

        // Conversion factors
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        elapsedTimeArray[0]= elapsedDays;

        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        elapsedTimeArray[1] = elapsedHours;

        long elapsedMinutes = difference / minutesInMilli;

        elapsedTimeArray[2] = elapsedMinutes;

        return elapsedTimeArray;
        }


    }


package com.aht.business.kirti.pasitupusi.model.utils;

import java.util.Calendar;

public class DateUtils {

    public static boolean isOldDate(String date, Calendar todayCalendar) {

        String[] dateSplit = date.split("-");

        if(dateSplit.length != 3) {
            return true;
        }

        int currentYear = todayCalendar.get(Calendar.YEAR);
        int currentMonth = todayCalendar.get(Calendar.MONTH) + 1; //Calendar.JANUARY;
        int curDate = todayCalendar.get(Calendar.DAY_OF_MONTH);

        if(currentYear > Integer.parseInt(dateSplit[0])) {
            return true;
        }
        if(currentYear == Integer.parseInt(dateSplit[0])
                && currentMonth > Integer.parseInt(dateSplit[1])) {
            return true;
        }
        if(currentYear == Integer.parseInt(dateSplit[0])
                && currentMonth == Integer.parseInt(dateSplit[1])
                && curDate > Integer.parseInt(dateSplit[2])) {
            return true;
        }

        return false;

    }

    public static boolean isOldDate(String date, String today) {

        String[] dateSplit = date.split("-");
        String[] todayDateSplit = today.split("-");

        if(dateSplit.length != 3 || todayDateSplit.length != 3) {
            return true;
        }

        int currentYear = Integer.parseInt(todayDateSplit[0]);
        int currentMonth = Integer.parseInt(todayDateSplit[1]);
        int curDate = Integer.parseInt(todayDateSplit[2]);

        if(currentYear > Integer.parseInt(dateSplit[0])) {
            return true;
        }
        if(currentYear == Integer.parseInt(dateSplit[0])
                && currentMonth > Integer.parseInt(dateSplit[1])) {
            return true;
        }
        if(currentYear == Integer.parseInt(dateSplit[0])
                && currentMonth == Integer.parseInt(dateSplit[1])
                && curDate > Integer.parseInt(dateSplit[2])) {
            return true;
        }

        return false;

    }
}

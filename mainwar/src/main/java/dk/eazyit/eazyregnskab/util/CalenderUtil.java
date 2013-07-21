package dk.eazyit.eazyregnskab.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author
 */
public class CalenderUtil {


    public static Date getFirstDayInYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    public static Date getFirstDayInYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    public static Date getLastDayInYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 11); // 11 = december
        cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
        return cal.getTime();
    }

    public static Date getLastDayInYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return cal.getTime();
    }

    public static Date add(Date date, int years, int months, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        cal.add(Calendar.YEAR, months);
        cal.add(Calendar.YEAR, days);
        return cal.getTime();
    }

    public static Date addOneYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    public static Date subtractOneYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public static String getSimpleDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        return sdf.format(date);
    }
}

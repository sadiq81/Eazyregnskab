package dk.eazyit.eazyregnskab.util;

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


    public static Date getLastDayInYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 11); // 11 = december
        cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
        return cal.getTime();
    }
}

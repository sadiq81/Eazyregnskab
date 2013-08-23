package dk.eazyit.eazyregnskab.util;

import dk.eazyit.eazyregnskab.domain.FiscalYear;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author
 */
public class CalendarUtil {


    public static Date getFirstDayInYear() {
        Calendar cal = Calendar.getInstance();
        resetCalender(cal);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    public static Date getFirstDayInYear(Date date) {
        Calendar cal = Calendar.getInstance();
        resetCalender(cal);
        cal.setTime(date);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    public static Date getLastDayInYear() {
        Calendar cal = Calendar.getInstance();
        resetCalender(cal);
        cal.set(Calendar.MONTH, 11); // 11 = december
        cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
        return cal.getTime();
    }

    public static Date getLastDayInYear(Date date) {
        Calendar cal = Calendar.getInstance();
        resetCalender(cal);
        cal.setTime(date);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return cal.getTime();
    }

    public static Date add(Date date, int years, int months, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        resetCalender(cal);
        cal.add(Calendar.YEAR, years);
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static Date addOneYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        resetCalender(cal);
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    public static Date subtractOneYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        resetCalender(cal);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public static String getSimpleDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        return sdf.format(date);
    }

    public static boolean betweenDates(Date testDate, FiscalYear fiscalYear) {
        DateTime date = new DateTime(testDate).toDateMidnight().toDateTime();
        testDate = date.toDate();
        boolean after = testDate.compareTo(fiscalYear.getStart()) >= 0;
        boolean before = testDate.compareTo(fiscalYear.getEnd()) <= 0;

        return after && before;
    }

    private static Calendar resetCalender(Calendar cal) {
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}

package dk.eazyit.eazyregnskab.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class BookingResult implements Serializable {

    BookingStatus bookingStatus = BookingStatus.SUCCESS;
    List<Integer> notInBalance = new ArrayList<Integer>();
    List<Integer> notInOpenFiscalYear = new ArrayList<Integer>();

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<Integer> getNotInBalance() {
        return notInBalance;
    }

    public void setNotInBalance(List<Integer> notInBalance) {
        this.notInBalance = notInBalance;
    }

    public List<Integer> getNotInOpenFiscalYear() {
        return notInOpenFiscalYear;
    }

    public void setNotInOpenFiscalYear(List<Integer> notInOpenFiscalYear) {
        this.notInOpenFiscalYear = notInOpenFiscalYear;
    }

    public String getListOfNotInBalance() {

        StringBuilder sb = new StringBuilder("");
        for (Integer integer : notInBalance) {
            sb.append(" " + integer.toString() + ",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        return sb.toString();
    }

    public String getListOfNotInFiscalYear() {

        StringBuilder sb = new StringBuilder("");
        for (Integer integer : notInOpenFiscalYear) {
            sb.append(" " + integer.toString() + ",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        return sb.toString();
    }
}

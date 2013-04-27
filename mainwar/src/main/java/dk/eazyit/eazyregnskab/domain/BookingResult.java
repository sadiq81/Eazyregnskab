package dk.eazyit.eazyregnskab.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class BookingResult implements Serializable {

    BookingStatus bookingStatus = BookingStatus.SUCCESS;
    List<Integer> list = new ArrayList<Integer>();

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public String getListOfErrors() {
        StringBuilder sb = new StringBuilder("");
        for (Integer integer : list) {
            sb.append(" " + integer.toString() + ",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }
}

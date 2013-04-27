package dk.eazyit.eazyregnskab.domain;

import java.util.List;

/**
 * @author
 */
public class BookingResult {

    BookingStatus bookingStatus;
    List<Integer> list;

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
}

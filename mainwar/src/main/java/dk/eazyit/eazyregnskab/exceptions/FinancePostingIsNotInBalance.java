package dk.eazyit.eazyregnskab.exceptions;

/**
 * @author
 */
public class FinancePostingIsNotInBalance extends Throwable {

    Integer bookingNumber;

    public FinancePostingIsNotInBalance(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }
}

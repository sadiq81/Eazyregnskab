package dk.eazyit.eazyregnskab.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * @author
 */
public class DraftFinancePostingBatch {

    public DraftFinancePostingBatch(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    private Integer bookingNumber;
    private List<DraftFinancePosting> list = new LinkedList<DraftFinancePosting>();

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public List<DraftFinancePosting> getList() {
        return list;
    }

    public void setList(List<DraftFinancePosting> list) {
        this.list = list;
    }
}

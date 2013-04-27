package dk.eazyit.eazyregnskab.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class BookedFinancePostingBatch implements Serializable {

    public BookedFinancePostingBatch(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    Integer bookingNumber;
    List<DraftFinancePosting> draftFinancePostingList = new ArrayList<DraftFinancePosting>();
    List<BookedFinancePosting> list = new ArrayList<BookedFinancePosting>();

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public List<DraftFinancePosting> getDraftFinancePostingList() {
        return draftFinancePostingList;
    }

    public void setDraftFinancePostingList(List<DraftFinancePosting> draftFinancePostingList) {
        this.draftFinancePostingList = draftFinancePostingList;
    }

    public List<BookedFinancePosting> getList() {
        return list;
    }

    public void setList(List<BookedFinancePosting> list) {
        this.list = list;
    }
}

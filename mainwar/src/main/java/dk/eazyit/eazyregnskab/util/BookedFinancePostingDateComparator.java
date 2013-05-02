package dk.eazyit.eazyregnskab.util;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;

import java.util.Comparator;

/**
 * @author
 */
public class BookedFinancePostingDateComparator implements Comparator<BookedFinancePosting> {
    @Override
    public int compare(BookedFinancePosting bookedFinancePosting, BookedFinancePosting bookedFinancePosting2) {
        return bookedFinancePosting.getDate().compareTo(bookedFinancePosting2.getDate());
    }
}

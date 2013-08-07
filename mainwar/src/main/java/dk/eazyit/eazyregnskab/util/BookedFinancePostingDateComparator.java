package dk.eazyit.eazyregnskab.util;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.domain.BookedFinancePostingType;

import java.util.Comparator;

/**
 * @author
 */
public class BookedFinancePostingDateComparator implements Comparator<BookedFinancePosting> {
    @Override
    public int compare(BookedFinancePosting bookedFinancePosting, BookedFinancePosting bookedFinancePosting2) {
        int compare = bookedFinancePosting.getDate().compareTo(bookedFinancePosting2.getDate());

        if (compare == 0) {
            return getWeightOfType(bookedFinancePosting.getBookedFinancePostingType()) - getWeightOfType(bookedFinancePosting2.getBookedFinancePostingType());
        } else {
            return compare;
        }
    }

    private int getWeightOfType(BookedFinancePostingType type) {

        switch (type) {
            case PRIMO: {
                return 0;
            }
            case NORMAL: {
                return 1;
            }
            default: {
                throw new NullPointerException("all types should be handled");
            }
        }

    }
}

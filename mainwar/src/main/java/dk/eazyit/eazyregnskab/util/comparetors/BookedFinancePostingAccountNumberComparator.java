package dk.eazyit.eazyregnskab.util.comparetors;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author
 */
public class BookedFinancePostingAccountNumberComparator implements Comparator<BookedFinancePosting>, Serializable {
    @Override
    public int compare(BookedFinancePosting bookedFinancePosting, BookedFinancePosting bookedFinancePosting2) {
        return bookedFinancePosting.getFinanceAccount().getAccountNumber() - bookedFinancePosting2.getFinanceAccount().getAccountNumber();
    }
}

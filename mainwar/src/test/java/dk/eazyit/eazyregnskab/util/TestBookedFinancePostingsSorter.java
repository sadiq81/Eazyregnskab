package dk.eazyit.eazyregnskab.util;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.domain.BookedFinancePostingType;
import dk.eazyit.eazyregnskab.util.comparetors.BookedFinancePostingDateComparator;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class TestBookedFinancePostingsSorter extends TestCase {

    public void testSorter() {


        List<BookedFinancePosting> list = new ArrayList<BookedFinancePosting>();
        Date date = new Date();

        list.add(new BookedFinancePosting().setBookedFinancePostingType(BookedFinancePostingType.NORMAL).setDate(date));
        list.add(new BookedFinancePosting().setBookedFinancePostingType(BookedFinancePostingType.NORMAL).setDate(date));
        list.add(new BookedFinancePosting().setBookedFinancePostingType(BookedFinancePostingType.NORMAL).setDate(date));
        list.add(new BookedFinancePosting().setBookedFinancePostingType(BookedFinancePostingType.NORMAL).setDate(date));
        list.add(new BookedFinancePosting().setBookedFinancePostingType(BookedFinancePostingType.PRIMO).setDate(date));

        Collections.sort(list, new BookedFinancePostingDateComparator());

        assertEquals(BookedFinancePostingType.PRIMO, list.get(0).getBookedFinancePostingType());


    }


}

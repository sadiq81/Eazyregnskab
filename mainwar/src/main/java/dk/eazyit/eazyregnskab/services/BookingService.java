package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.BookedFinancePostingDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.DraftFinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author
 */
@Service
public class BookingService {


    @Autowired
    BookedFinancePostingDAO bookedFinancePostingDAO;
    @Autowired
    DraftFinancePostingDAO draftFinancePostingDAO;
    @Autowired
    FinanceAccountService financeAccountService;

    @Transactional
    public void BookChosen(DailyLedger dailyLedger, BookingResult result) {
        List<DraftFinancePosting> draftFinancePostingList = financeAccountService.findPostingsFromDailyLedger(dailyLedger);
        List<DraftFinancePosting> markedForSave = new ArrayList<DraftFinancePosting>();
        for (DraftFinancePosting draftFinancePosting : draftFinancePostingList) {
            if (draftFinancePosting.isChosen()) {
                markedForSave.add(draftFinancePosting);
            }
        }
        createBookedFinancePostings(markedForSave, result);
    }

    @Transactional
    public void BookAll(DailyLedger dailyLedger, BookingResult result) {
        List<DraftFinancePosting> draftFinancePostingList = financeAccountService.findPostingsFromDailyLedger(dailyLedger);
        createBookedFinancePostings(draftFinancePostingList, result);
    }


    //TODO create test to verify all types of transactions.
    private void createBookedFinancePostings(List<DraftFinancePosting> draftFinancePostings, BookingResult result) {

        Map<Integer, BookedFinancePostingBatch> map = new HashMap<Integer, BookedFinancePostingBatch>();

        for (DraftFinancePosting draftFinancePosting : draftFinancePostings) {

            List<BookedFinancePosting> list = createBookedFinancePostings(draftFinancePosting);
            BookedFinancePostingBatch batch = map.get(draftFinancePosting.getBookingNumber());

            if (batch == null) {
                batch = new BookedFinancePostingBatch(draftFinancePosting.getBookingNumber());
            }
            batch.getDraftFinancePostingList().add(draftFinancePosting);
            batch.getList().addAll(list);
            map.put(batch.getBookingNumber(), batch);
        }

        checkBookedFinancePostingsForBalance(map, result);
    }

    private void checkBookedFinancePostingsForBalance(Map<Integer, BookedFinancePostingBatch> map, BookingResult result) {

        Map<Integer, BookedFinancePostingBatch> toBedSaved = new HashMap<Integer, BookedFinancePostingBatch>(map);

        for (Map.Entry<Integer, BookedFinancePostingBatch> entry : map.entrySet()) {

            BookingStatus bookingStatus = BookingStatus.SUCCESS;

            Double amount = new Double(0);
            List<Date> dateList = new ArrayList<Date>();

            for (BookedFinancePosting bookedFinancePosting : entry.getValue().getList()) {
                amount += bookedFinancePosting.getAmount();
                dateList.add(bookedFinancePosting.getDate());
            }

            //Check balance of posting
            if (amount != 0) {
                bookingStatus = BookingStatus.ERROR;
            }
            //Check same date
            Date prev = null;
            for (Date date : dateList) {

                if (prev == null) prev = date;

                if (date.compareTo(prev) != 0) {
                    bookingStatus = BookingStatus.ERROR;
                    break;
                }
                prev = date;
            }

            //Remove if error
            if (bookingStatus == BookingStatus.ERROR) {
                result.setBookingStatus(BookingStatus.ERROR);
                result.getList().add(entry.getKey());
                toBedSaved.remove(entry.getKey());
            }
        }
        saveCheckedBookedFinancePostings(toBedSaved);
    }

    private void saveCheckedBookedFinancePostings(Map<Integer, BookedFinancePostingBatch> map) {

        for (Map.Entry<Integer, BookedFinancePostingBatch> entry : map.entrySet()) {
            for (BookedFinancePosting bookedFinancePosting : entry.getValue().getList()) {
                bookedFinancePostingDAO.save(bookedFinancePosting);
            }
            for (DraftFinancePosting draftFinancePosting : entry.getValue().getDraftFinancePostingList()) {
                draftFinancePostingDAO.delete(draftFinancePosting);
            }
        }
    }

    private List<BookedFinancePosting> createBookedFinancePostings(DraftFinancePosting draftFinancePosting) {

        List<BookedFinancePosting> list = new LinkedList<BookedFinancePosting>();

        FinanceAccount account = draftFinancePosting.getFinanceAccount();
        if (account != null) {
            BookedFinancePosting posting = setupBaseData(draftFinancePosting);
            posting.setAmount(draftFinancePosting.getAmount());
            posting.setFinanceAccount(account);
            list.add(posting);

            VatType vatType = draftFinancePosting.getVatType();
            if (vatType != null) {

                double vat = getVat(draftFinancePosting.getAmount(), vatType.getPercentage());

                if (vatType.isReverse()) {

                    BookedFinancePosting vatPosting = setupBaseData(draftFinancePosting);
                    vatPosting.setAmount(vat);
                    vatPosting.setFinanceAccount(vatType.getFinanceAccount());
                    list.add(vatPosting);

                    BookedFinancePosting reverseVatPosting = setupBaseData(draftFinancePosting);
                    reverseVatPosting.setAmount(0 - vat);
                    reverseVatPosting.setFinanceAccount(vatType.getFinanceAccountReverse());
                    list.add(reverseVatPosting);

                } else {

                    BookedFinancePosting vatPosting = setupBaseData(draftFinancePosting);
                    vatPosting.setAmount(vat);
                    posting.removeVat(vat);
                    vatPosting.setFinanceAccount(vatType.getFinanceAccount());
                    list.add(vatPosting);
                }
            }
        }

        FinanceAccount reverse = draftFinancePosting.getReverseFinanceAccount();
        if (reverse != null) {
            list.addAll(createBookedFinancePostings(draftFinancePosting.getPostingForReverse()));
        }

        return list;
    }

    private BookedFinancePosting setupBaseData(DraftFinancePosting draftFinancePosting) {
        BookedFinancePosting bookedFinancePosting = new BookedFinancePosting();
        bookedFinancePosting.setDate(draftFinancePosting.getDate());
        bookedFinancePosting.setBookingNumber(draftFinancePosting.getBookingNumber());
        bookedFinancePosting.setText(draftFinancePosting.getText());
        return bookedFinancePosting;
    }

    private double getVat(double amount, double percentageInHundreds) {
        return amount - (amount / (1 + (percentageInHundreds / 100)));
    }

}

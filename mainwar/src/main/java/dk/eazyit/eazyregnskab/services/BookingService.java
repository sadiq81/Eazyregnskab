package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.BookedFinancePostingDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.DraftFinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.util.CalendarUtil;
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
    @Autowired
    PostingService postingService;
    @Autowired
    FiscalYearService fiscalYearService;
    @Autowired
    VatTypeService vatTypeService;

    @Transactional
    public void BookDailyLedger(DailyLedger dailyLedger, BookingResult result, boolean bookAll) {

        List<DraftFinancePosting> draftFinancePostingList = postingService.findDraftPostingsFromDailyLedger(dailyLedger);

        List<DraftFinancePosting> markedForSave = bookAll ? draftFinancePostingList : removeNotMarkedForSave(draftFinancePostingList);

        List<DraftFinancePosting> withinOpenFiscalYears = checkWithinOpenFiscalYear(markedForSave, dailyLedger, result);

        Map<Integer, BookedFinancePostingBatch> notCheckBookedFinancePostings = createBookedFinancePostings(withinOpenFiscalYears);

        Map<Integer, BookedFinancePostingBatch> inBalance = checkBookedFinancePostingsForBalance(notCheckBookedFinancePostings, result);

        saveCheckedBookedFinancePostings(inBalance);

    }

    private List<DraftFinancePosting> removeNotMarkedForSave(List<DraftFinancePosting> draftFinancePostings) {

        List<DraftFinancePosting> markedForSave = new ArrayList<DraftFinancePosting>();

        for (DraftFinancePosting draftFinancePosting : draftFinancePostings) {
            if (draftFinancePosting.isChosen()) {
                markedForSave.add(draftFinancePosting);
            }
        }

        return markedForSave;
    }


    private List<DraftFinancePosting> checkWithinOpenFiscalYear(List<DraftFinancePosting> draftFinancePostings, DailyLedger dailyLedger, BookingResult bookingResult) {

        List<FiscalYear> fiscalYears = fiscalYearService.findOpenFiscalYearByLegalEntity(dailyLedger.getLegalEntity());
        List<DraftFinancePosting> isWithOpenFiscalYear = new ArrayList<DraftFinancePosting>();

        for (DraftFinancePosting draftFinancePosting : draftFinancePostings) {

            boolean isWithIn = false;
            for (FiscalYear fiscalYear : fiscalYears) {
                if (CalendarUtil.betweenDates(draftFinancePosting.getDate(), fiscalYear)) {
                    isWithOpenFiscalYear.add(draftFinancePosting);
                    isWithIn = true;
                    break;
                }
            }
            if (!isWithIn) {
                bookingResult.getNotInOpenFiscalYear().add(draftFinancePosting.getBookingNumber());
                bookingResult.setBookingStatus(BookingStatus.ERROR);
            }

        }
        return isWithOpenFiscalYear;
    }


    private Map<Integer, BookedFinancePostingBatch> createBookedFinancePostings(List<DraftFinancePosting> draftFinancePostings) {

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

        return map;
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
                    vatPosting.setVatType(vatType);
                    list.add(vatPosting);

                    BookedFinancePosting reverseVatPosting = setupBaseData(draftFinancePosting);
                    reverseVatPosting.setAmount(0 - vat);
                    reverseVatPosting.setFinanceAccount(vatType.getFinanceAccountReverse());
                    reverseVatPosting.setVatType(vatType);
                    list.add(reverseVatPosting);

                } else {

                    BookedFinancePosting vatPosting = setupBaseData(draftFinancePosting);
                    vatPosting.setAmount(vat);
                    posting.removeVat(vat);
                    vatPosting.setFinanceAccount(vatType.getFinanceAccount());
                    vatPosting.setVatType(vatType);
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
        bookedFinancePosting.setBookedFinancePostingType(BookedFinancePostingType.NORMAL);
        bookedFinancePosting.setDate(draftFinancePosting.getDate());
        bookedFinancePosting.setBookingNumber(draftFinancePosting.getBookingNumber());
        bookedFinancePosting.setText(draftFinancePosting.getText());
        return bookedFinancePosting;
    }

    private double getVat(double amount, double percentageInHundreds) {
        return amount - (amount / (1 + (percentageInHundreds / 100)));
    }


    private Map<Integer, BookedFinancePostingBatch> checkBookedFinancePostingsForBalance(Map<Integer, BookedFinancePostingBatch> map, BookingResult result) {

        Map<Integer, BookedFinancePostingBatch> isInBalance = new HashMap<Integer, BookedFinancePostingBatch>(map);

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
                result.getNotInBalance().add(entry.getKey());
                isInBalance.remove(entry.getKey());
            }
        }
        return isInBalance;
    }

    private void saveCheckedBookedFinancePostings(Map<Integer, BookedFinancePostingBatch> clearedAllChecks) {

        for (Map.Entry<Integer, BookedFinancePostingBatch> entry : clearedAllChecks.entrySet()) {
            for (BookedFinancePosting bookedFinancePosting : entry.getValue().getList()) {

                if (!bookedFinancePosting.getFinanceAccount().isInUse()) {
                    financeAccountService.setFinanceAccountInUse(bookedFinancePosting.getFinanceAccount());
                }
                if (bookedFinancePosting.getVatType() != null && !bookedFinancePosting.getVatType().isInUse()) {
                    vatTypeService.setVatTypeInUse(bookedFinancePosting.getVatType());
                }

                bookedFinancePostingDAO.save(bookedFinancePosting);
            }
            for (DraftFinancePosting draftFinancePosting : entry.getValue().getDraftFinancePostingList()) {
                draftFinancePostingDAO.delete(draftFinancePosting);
            }
        }
    }


}

package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.BookedFinancePostingDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.DraftFinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.util.CalendarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author
 */
@Service
public class BookingService {

    final static Logger logger = LoggerFactory.getLogger(BookingService.class);

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
    @Autowired
    DailyLedgerService dailyLedgerService;

    @Transactional
    public void bookDailyLedger(DailyLedger dailyLedger, BookingResult result, boolean bookAll) {

        logger.debug("Booking dailyledger " + dailyLedger + " with setting bookAll = " + bookAll);
        List<DraftFinancePosting> draftFinancePostingList = postingService.findDraftPostingsFromDailyLedger(dailyLedger);
        logger.debug("Found " + draftFinancePostingList.size() + " postings in dailyledger " + dailyLedger);

        List<DraftFinancePosting> markedForSave = bookAll ? draftFinancePostingList : removeNotMarkedForSave(draftFinancePostingList);
        logger.debug(markedForSave.size() + " postings in dailyledger " + dailyLedger + " where marked for save");

        List<DraftFinancePosting> withinOpenFiscalYears = checkWithinOpenFiscalYear(markedForSave, dailyLedger, result);
        logger.debug(markedForSave.size() + " postings in dailyledger " + dailyLedger + " where within open fiscal year");

        logger.debug("Creating booked postings");
        Map<Integer, BookedFinancePostingBatch> notCheckBookedFinancePostings = createBookedFinancePostings(withinOpenFiscalYears);

        logger.debug("Checking postings for balance");
        Map<Integer, BookedFinancePostingBatch> inBalance = checkBookedFinancePostingsForBalance(notCheckBookedFinancePostings, result);

        logger.debug("Saving postings");
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

            logger.debug("Creating booked postings from " + draftFinancePosting);
            List<BookedFinancePosting> list = createBookedFinancePostings(draftFinancePosting);
            logger.debug(list + " booked postings where created from " + draftFinancePosting);
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
        BookedFinancePosting posting = null;
        BookedFinancePosting reversePosting;

        FinanceAccount account = draftFinancePosting.getFinanceAccount();
        if (account != null) {

            posting = setupBaseData(draftFinancePosting);
            posting.setAmount(draftFinancePosting.getAmount());
            posting.setFinanceAccount(account);
            list.add(posting);
            logger.debug("Created posting " + posting);

            VatType vatType = draftFinancePosting.getVatType();
            if (vatType != null) {

                double vat = getVat(draftFinancePosting.getAmount(), vatType.getPercentage());

                if (vatType.isReverse()) {

                    BookedFinancePosting vatPosting = setupBaseData(draftFinancePosting);
                    vatPosting.setAmount(vat);
                    vatPosting.setFinanceAccount(vatType.getFinanceAccount());
                    vatPosting.setVatType(vatType);
                    logger.debug("Created vatposting " + posting);
                    list.add(vatPosting);

                    posting.setVatPosting(vatPosting);

                    BookedFinancePosting reverseVatPosting = setupBaseData(draftFinancePosting);
                    reverseVatPosting.setAmount(0 - vat);
                    reverseVatPosting.setFinanceAccount(vatType.getFinanceAccountReverse());
                    reverseVatPosting.setVatType(vatType);
                    logger.debug("Created reverseVatPosting " + posting);
                    list.add(reverseVatPosting);

                    posting.setReverseVatPosting(reverseVatPosting);

                } else {

                    BookedFinancePosting vatPosting = setupBaseData(draftFinancePosting);
                    vatPosting.setAmount(vat);
                    posting.removeVat(vat);
                    vatPosting.setFinanceAccount(vatType.getFinanceAccount());
                    vatPosting.setVatType(vatType);
                    logger.debug("Created vat posting " + posting);
                    list.add(vatPosting);

                    posting.setVatPosting(vatPosting);
                }
            }
        }

        FinanceAccount reverse = draftFinancePosting.getReverseFinanceAccount();

        if (reverse != null) {

            reversePosting = setupBaseData(draftFinancePosting);
            reversePosting.setAmount(0 - draftFinancePosting.getAmount());
            reversePosting.setFinanceAccount(reverse);

            logger.debug("Created reverse Posting " + posting);
            list.add(reversePosting);

            VatType vatType = draftFinancePosting.getReverseVatType();
            if (vatType != null) {

                double vat = getVat(0 - draftFinancePosting.getAmount(), vatType.getPercentage());

                if (vatType.isReverse()) {

                    BookedFinancePosting vatPosting = setupBaseData(draftFinancePosting);
                    vatPosting.setAmount(vat);
                    vatPosting.setFinanceAccount(vatType.getFinanceAccount());
                    vatPosting.setVatType(vatType);
                    logger.debug("Created reverse vat posting " + posting);
                    list.add(vatPosting);

                    reversePosting.setVatPosting(vatPosting);

                    BookedFinancePosting reverseVatPosting = setupBaseData(draftFinancePosting);
                    reverseVatPosting.setAmount(0 - vat);
                    reverseVatPosting.setFinanceAccount(vatType.getFinanceAccountReverse());
                    reverseVatPosting.setVatType(vatType);
                    logger.debug("Created reverse vat reverse posting " + posting);
                    list.add(reverseVatPosting);

                    reversePosting.setReverseVatPosting(reverseVatPosting);

                } else {

                    BookedFinancePosting vatPosting = setupBaseData(draftFinancePosting);
                    vatPosting.setAmount(vat);
                    reversePosting.removeVat(vat);
                    vatPosting.setFinanceAccount(vatType.getFinanceAccount());
                    vatPosting.setVatType(vatType);
                    logger.debug("Created reverse vat posting " + posting);
                    list.add(vatPosting);

                    reversePosting.setVatPosting(vatPosting);
                }
            }
            if (posting != null) {
                posting.setReversePosting(reversePosting);
                reversePosting.setReverse(true);
            }
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
        double vat = amount - (amount / (1 + (percentageInHundreds / 100)));
        logger.debug("Calculating vat from " + amount + " with vat percentage" + percentageInHundreds + " vat is " + vat);
        return vat;
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
                logger.debug("Amount of posting " + entry.getValue().getBookingNumber() + " is not in balance");
                bookingStatus = BookingStatus.ERROR;
            }
            //Check same date
            Date prev = null;
            for (Date date : dateList) {

                if (prev == null) prev = date;

                if (date.compareTo(prev) != 0) {
                    bookingStatus = BookingStatus.ERROR;
                    logger.debug("Date of posting " + entry.getValue().getBookingNumber() + " is not in balance");
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

                if (bookedFinancePosting.getVatPosting() != null && bookedFinancePosting.getVatPosting().getId().equals(0L)) {
                    bookedFinancePostingDAO.create(bookedFinancePosting.getVatPosting());
                }
                if (bookedFinancePosting.getReverseVatPosting() != null && bookedFinancePosting.getReverseVatPosting().getId().equals(0L)) {
                    bookedFinancePostingDAO.create(bookedFinancePosting.getReverseVatPosting());
                }
                if (bookedFinancePosting.getReversePosting() != null && bookedFinancePosting.getReversePosting().getId().equals(0L)) {
                    bookedFinancePostingDAO.create(bookedFinancePosting.getReversePosting());
                }

                if (bookedFinancePosting.getId().equals(0L)) bookedFinancePostingDAO.create(bookedFinancePosting);

            }

            for (DraftFinancePosting draftFinancePosting : entry.getValue().getDraftFinancePostingList()) {
                draftFinancePostingDAO.delete(draftFinancePosting);
            }
        }
    }

    @Transactional
    public void regretPostings(List<BookedFinancePosting> list, DailyLedger dailyLedger) {
        logger.debug("Posting number " + list.get(0).getBookingNumber() + " is being regret");
        for (DraftFinancePosting draftFinancePosting : createNewFromListOfBookedFinancePostings(list, dailyLedger)) {
            draftFinancePostingDAO.create(draftFinancePosting.reverseAmount());
        }
    }

    @Transactional
    public void copyPostings(List<BookedFinancePosting> list, DailyLedger dailyLedger) {
        logger.debug("Posting number " + list.get(0).getBookingNumber() + " is being copyed");
        for (DraftFinancePosting draftFinancePosting : createNewFromListOfBookedFinancePostings(list, dailyLedger)) {
            draftFinancePostingDAO.create(draftFinancePosting);
        }
    }

    @Transactional
    public void flipPostings(List<BookedFinancePosting> list, DailyLedger dailyLedger) {
        logger.debug("Posting number " + list.get(0).getBookingNumber() + " is being flipped");
        for (DraftFinancePosting draftFinancePosting : createNewFromListOfBookedFinancePostings(list, dailyLedger)) {
            draftFinancePostingDAO.create(draftFinancePosting.reverseAmount());
        }
        for (DraftFinancePosting draftFinancePosting : createNewFromListOfBookedFinancePostings(list, dailyLedger)) {
            draftFinancePostingDAO.create(draftFinancePosting.reverseAmount());
        }
    }

    private List<DraftFinancePosting> createNewFromListOfBookedFinancePostings(List<BookedFinancePosting> list, DailyLedger dailyLedger) {
        List<DraftFinancePosting> draftFinancePostingList = new ArrayList<DraftFinancePosting>();

        List<BookedFinancePosting> noneVatPostings = new ArrayList<BookedFinancePosting>();
        for (BookedFinancePosting posting : list) {
            if (posting.getVatType() == null) {
                noneVatPostings.add(posting);
            }
        }

        for (BookedFinancePosting posting : noneVatPostings) {

            if (!posting.isReverse()) {
                DraftFinancePosting draft = new DraftFinancePosting(posting).setDailyLedger(dailyLedger);
                draftFinancePostingList.add(draft);

                if (posting.getVatPosting() != null && posting.getReverseVatPosting() == null) {
                    draft.setVatType(posting.getVatPosting().getVatType());
                    draft.addAmount(posting.getVatPosting().getAmount());
                } else if (posting.getVatPosting() != null && posting.getReverseVatPosting() != null) {
                    draft.setVatType(posting.getVatPosting().getVatType());
                }

                if (posting.getReversePosting() != null) {
                    draft.setReverseFinanceAccount(posting.getReversePosting().getFinanceAccount());

                    if (posting.getReversePosting().getVatPosting() != null) {
                        draft.setReverseVatType(posting.getReversePosting().getVatPosting().getVatType());
                    }
                }
            }
        }

        return draftFinancePostingList;
    }
}

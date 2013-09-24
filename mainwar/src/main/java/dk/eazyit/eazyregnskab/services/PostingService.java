package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.BookedFinancePostingDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.DraftFinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author
 */
@Service("postingService")
public class PostingService {

    private Logger LOG = LoggerFactory.getLogger(PostingService.class);

    @Autowired
    private DraftFinancePostingDAO draftFinancePostingDAO;
    @Autowired
    private BookedFinancePostingDAO bookedFinancePostingDAO;

    @Transactional
    public int countDraftFinancePostingOfDailyLedger(DailyLedger dailyLedger) {
        LOG.debug("Couting all DraftFinancePosting from dailyLedger " + dailyLedger.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger).size();
    }

    @Transactional(readOnly = true)
    public List<DraftFinancePosting> findDraftPostingsFromAccount(FinanceAccount financeAccount) {
        LOG.debug("Finding all DraftFinancePosting from FinanceAccount " + financeAccount.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, financeAccount);
    }

    @Transactional(readOnly = true)
    public List<DraftFinancePosting> findDraftPostingsFromAccountSubList(FinanceAccount financeAccount, int first, int count) {
        LOG.debug("Finding all DraftFinancePosting from FinanceAccount " + financeAccount.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, new Integer(first), new Integer(count), financeAccount);
    }

    @Transactional(readOnly = true)
    public List<DraftFinancePosting> findDraftPostingsFromDailyLedger(DailyLedger dailyLedger) {
        LOG.debug("Finding all DraftFinancePosting from DailyLedger " + dailyLedger.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger);
    }

    @Transactional
    public List<DraftFinancePosting> findDraftPostingByDailyLedgerSubList(DailyLedger dailyLedger, int first, int count) {
        LOG.debug("Finding all DraftFinancePosting from DailyLedger starting with " + first + " to  " + count + " from " + dailyLedger.toString());
        List<DraftFinancePosting> list = draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, new Integer(first), new Integer(count), dailyLedger);
        return list;
    }

    @Transactional
    public List<DraftFinancePosting> findDraftPostingByDailyLedgerSubListSortBy(DailyLedger dailyLedger, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all DraftFinancePosting from DailyLedger starting with " + first + " to  " + count + " from " + dailyLedger.toString());
        List<DraftFinancePosting> list = draftFinancePostingDAO.findByNamedQuerySorted(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, new Integer(first), new Integer(count), sortProperty, Ascending, dailyLedger);
        return list;
    }

    @Transactional
    public List<DraftFinancePosting> findDraftFinancePostingsByVatType(VatType vatType) {
        LOG.debug("Finding all DraftFinancePosting with VatType" + vatType.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, vatType);
    }

    public List<DraftFinancePosting> findDraftFinancePostingsByVatTypeSubList(VatType vatType, int first, int count) {
        LOG.debug("Finding all DraftFinancePosting with VatType" + vatType.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, new Integer(first), new Integer(count), vatType);
    }

    @Transactional
    public List<DraftFinancePosting> findDraftFinancePostingsByYear(FiscalYear fiscalYear) {
        LOG.debug("Finding all DraftFinancePosting with VatType" + fiscalYear.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_IN_YEAR, fiscalYear.getLegalEntity(), fiscalYear.getStart(), fiscalYear.getEnd());
    }

    @Transactional
    public List<DraftFinancePosting> findDraftFinancePostingsByYearSubList(FiscalYear fiscalYear, int first, int count) {
        LOG.debug("Finding all DraftFinancePosting with VatType" + fiscalYear.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_IN_YEAR, new Integer(first), new Integer(count), fiscalYear.getLegalEntity(), fiscalYear.getStart(), fiscalYear.getEnd());
    }

    @Transactional
    public List<BookedFinancePosting> findBookedFinancePostingsByYear(FiscalYear fiscalYear) {
        LOG.debug("Finding all DraftFinancePosting with VatType" + fiscalYear.toString());
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_IN_YEAR, fiscalYear.getLegalEntity(), fiscalYear.getStart(), fiscalYear.getEnd());
    }

    @Transactional
    public List<BookedFinancePosting> findBookedFinancePostingsByYearSubList(FiscalYear fiscalYear, int first, int count) {
        LOG.debug("Finding all DraftFinancePosting with VatType" + fiscalYear.toString());
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_IN_YEAR, new Integer(first), new Integer(count), fiscalYear.getLegalEntity(), fiscalYear.getStart(), fiscalYear.getEnd());
    }

    @Transactional
    public void deleteFinancePosting(DraftFinancePosting draftFinancePosting) {
        draftFinancePostingDAO.delete(draftFinancePosting);
    }

    @Transactional
    public void saveDraftFinancePosting(DraftFinancePosting draftFinancePosting) {
        if (draftFinancePosting.getId() == 0) {
            draftFinancePostingDAO.create(draftFinancePosting);
        } else {
            draftFinancePostingDAO.save(draftFinancePosting);
        }
    }

    @Transactional
    public List<BookedFinancePosting> findBookedFinancePostingsByVatType(VatType vatType) {
        LOG.debug("Finding all BookedFinancePosting by vat type " + vatType);
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, vatType);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findBookedPostingsFromFinanceAccount(FinanceAccount financeAccount) {
        LOG.debug("Finding all BookedFinancePosting from FinanceAccount " + financeAccount.toString());
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, financeAccount);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findBookedPostingsFromFinanceAccountFromDateToDate(FinanceAccount account, Date fromDate, Date toDate) {
        LOG.debug("Finding all BookedFinancePosting from FinanceAccount " + account + " from date " + fromDate + " to date " + toDate);
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_ACCOUNT_FROM_DATE_TO_DATE,
                account, fromDate, toDate);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findBookedPostingsFromLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all BookedFinancePosting from LegalEntity " + legalEntity.toString());
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_LEGAL_ENTITY, legalEntity);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findBookedPostingsFromLegalEntityFromDateToDate(LegalEntity legalEntity, Date from, Date to) {
        LOG.debug("Finding all BookedFinancePosting from LegalEntity " + legalEntity.toString() + " from date " + from + " to date " + to);
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_LEGAL_ENTITY_FROM_DATE_TO_DATE, legalEntity, from, to);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findBookedPostingsFromLegalEntityByIntervalFromDateToDate(List<FinanceAccount> financeAccounts, Date from, Date to) {
        LOG.debug("Finding all BookedFinancePosting from accounts " + " from date " + from + " to date " + to);
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_INTERVAL_AND_DATE, financeAccounts, from, to);
    }

    @Transactional(readOnly = true)
    public BookedFinancePosting findPrimoPostingsFromAccount(FinanceAccount financeAccount, Date start) {
        LOG.debug("Finding primo BookedFinancePosting from account" + financeAccount.toString() + " start date " + start);
        return bookedFinancePostingDAO.findByNamedQueryUnique(BookedFinancePosting.QUERY_FIND_PRIMO_FINANCE_POSTING_BY_ACCOUNT, financeAccount, start, BookedFinancePostingType.PRIMO);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findBookedPostingsFromLegalEntityByBookingNumber(LegalEntity legalEntity, BookedFinancePosting bookedFinancePosting) {
        LOG.debug("Finding  BookedFinancePosting with number " + bookedFinancePosting.getBookingNumber());
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_BOOKING_NUMBER, legalEntity, bookedFinancePosting.getDate(), bookedFinancePosting.getBookingNumber());
    }

    @Transactional
    public void saveBookedFinancePosting(BookedFinancePosting bookedFinancePosting) {
        if (bookedFinancePosting.getId() == 0) {
            bookedFinancePostingDAO.create(bookedFinancePosting);
        } else {
            bookedFinancePostingDAO.save(bookedFinancePosting);
        }
    }

    @Transactional
    public void deleteBookedPosting(BookedFinancePosting bookedFinancePosting) {
        if (!BookedFinancePostingType.PRIMO.equals(bookedFinancePosting.getBookedFinancePostingType())) {
            throw new NullPointerException("MUST NEVER DELETE BOOKED FINANCEPOSTING");
        }
        bookedFinancePostingDAO.delete(bookedFinancePosting);
    }

    @Transactional
    public void clearDailyLedger(DailyLedger currentDailyLedger) {
        LOG.debug("Clearing dailyledger " + currentDailyLedger);
        for (DraftFinancePosting draftFinancePosting : findDraftPostingsFromDailyLedger(currentDailyLedger)) {
            draftFinancePostingDAO.delete(draftFinancePosting);
        }
    }
}

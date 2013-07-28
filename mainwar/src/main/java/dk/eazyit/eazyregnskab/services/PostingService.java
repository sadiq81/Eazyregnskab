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
    public int countFinancePostingOfDailyLedger(DailyLedger dailyLedger) {
        LOG.debug("Couting all DraftFinancePosting from dailyLedger " + dailyLedger.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger).size();
    }

    @Transactional(readOnly = true)
    public List<DraftFinancePosting> findPostingsFromAccount(FinanceAccount financeAccount) {
        LOG.debug("Finding all DraftFinancePosting from FinanceAccount " + financeAccount.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, financeAccount);
    }

    @Transactional(readOnly = true)
    public List<DraftFinancePosting> findPostingsFromDailyLedger(DailyLedger dailyLedger) {
        LOG.debug("Finding all DraftFinancePosting from DailyLedger " + dailyLedger.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger);
    }

    @Transactional
    public List<DraftFinancePosting> findFinancePostingByDailyLedgerSubList(DailyLedger dailyLedger, int first, int count) {
        LOG.debug("Finding all DraftFinancePosting from DailyLedger starting with " + first + " to  " + count + " from " + dailyLedger.toString());
        List<DraftFinancePosting> list = draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, new Integer(first), new Integer(count), dailyLedger);
        return list;
    }

    @Transactional
    public List<DraftFinancePosting> findFinancePostingByDailyLedgerSubListSortBy(DailyLedger dailyLedger, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all DraftFinancePosting from DailyLedger starting with " + first + " to  " + count + " from " + dailyLedger.toString());
        List<DraftFinancePosting> list = draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, new Integer(first), new Integer(count), sortProperty, Ascending, dailyLedger);
        return list;
    }

    @Transactional
    public List<DraftFinancePosting> findDraftFinancePostingsByVatType(VatType vatType) {
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, vatType);
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
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, vatType);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findPostingsFromFinanceAccount(FinanceAccount financeAccount) {
        LOG.debug("Finding all BookedFinancePosting from FinanceAccount " + financeAccount.toString());
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, financeAccount);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findPostingsFromFinanceAccountFromDateToDate(FinanceAccount account, Date fromDate, Date toDate) {
        LOG.debug("Finding all BookedFinancePosting from FinanceAccount " + account + " from date " + fromDate + " to date " + toDate);
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_ACCOUNT_FROM_DATE_TO_DATE,
                account, fromDate, toDate);
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> findPostingsFromLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all BookedFinancePosting from LegalEntity " + legalEntity.toString());
        return bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_LEGAL_ENTITY, legalEntity);
    }

    @Transactional(readOnly = true)
    public BookedFinancePosting findPrimoPostingsFromAccount(FinanceAccount financeAccount, Date start) {
        LOG.debug("Finding primo BookedFinancePosting from account" + financeAccount.toString());
        return bookedFinancePostingDAO.findByNamedQueryUnique(BookedFinancePosting.QUERY_FIND_PRIMO_FINANCE_POSTING_BY_ACCOUNT, financeAccount, start, BookedFinancePostingType.PRIMO);
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
        if (bookedFinancePosting.getBookedFinancePostingType() != BookedFinancePostingType.PRIMO) {
            throw new NullPointerException("MUST NEVER DELETE BOOKED FINANCEPOSTING");
        }
        bookedFinancePostingDAO.delete(bookedFinancePosting);
    }
}

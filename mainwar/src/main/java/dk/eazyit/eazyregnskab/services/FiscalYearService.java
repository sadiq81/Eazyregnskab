package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.FiscalYearDAO;
import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.util.CalendarUtil;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
@Service("fiscalYearService")
public class FiscalYearService {

    private Logger log = LoggerFactory.getLogger(FiscalYearService.class);

    @Autowired
    FiscalYearDAO fiscalYearDAO;
    @Autowired
    ReportService reportService;
    @Autowired
    FinanceAccountService financeAccountService;
    @Autowired
    PostingService postingService;

    @Transactional
    public boolean save(FiscalYear fiscalYear) {
        if (fiscalYear.getId() != null && fiscalYear.getId() != 0) {
            fiscalYearDAO.save(fiscalYear);
        } else {
            fiscalYearDAO.create(fiscalYear);
        }
        return true;
    }

    @Transactional
    public boolean isDeleteFiscalYearAllowed(FiscalYear fiscalYear) {
        return !fiscalYear.isOpen() &&
                postingService.findDraftFinancePostingsByYearSubList(fiscalYear, 0, 1).size() == 0 &&
                postingService.findBookedFinancePostingsByYearSubList(fiscalYear, 0, 1).size() == 0;
    }


    @Transactional
    public void deleteFiscalYear(FiscalYear fiscalYear) {
        fiscalYearDAO.delete(fiscalYear);
    }

    @Transactional
    public void openFiscalYear(FiscalYear fiscalYear) {

        List<FinanceAccount> financeAccounts = financeAccountService.findBookableFinanceAccountByLegalEntity(fiscalYear.getLegalEntity());

        //TODO find all primo in same transaction.
        for (FinanceAccount financeAccount : financeAccounts) {
            BookedFinancePosting primo = postingService.findPrimoPostingsFromAccount(financeAccount, CalendarUtil.add(fiscalYear.getEnd(), 0, 0, 1));
            if (primo != null) postingService.deleteBookedPosting(primo);
        }

        fiscalYear.setFiscalYearStatus(FiscalYearStatus.OPEN);
        save(fiscalYear);
        log.info("opened fiscal year " + fiscalYear);

    }

    @Transactional
    public void closeFiscalYear(FiscalYear fiscalYear, Date nextYearStart) {

        if (CalendarUtil.add(fiscalYear.getEnd(), 0, 0, 1).compareTo(nextYearStart) != 0) {
            throw new NullPointerException("Next year must be 1 day after previous");
        }

        List<FinanceAccount> financeAccountsWithSum = reportService.getFinanceAccountsWithSum(fiscalYear);

        FinanceAccount year_end_account = financeAccountService.findSystemFinanceAccountByLegalEntity(fiscalYear.getLegalEntity(), FinanceAccountType.YEAR_END);
        if (year_end_account == null) throw new NullPointerException("Year end must not be null");

        Double resultOfYear = new Double(0);
        List<BookedFinancePosting> primoPostings = new ArrayList<BookedFinancePosting>();

        for (FinanceAccount financeAccount : financeAccountsWithSum) {

            switch (financeAccount.getFinanceAccountType()) {
                case PROFIT:
                case EXPENSE: {
                }
                case YEAR_END: {
                    resultOfYear = resultOfYear + financeAccount.getSum();
                    break;
                }
                case ASSET:
                case LIABILITY: {
                    if (!financeAccount.getSum().equals(new Double(0))) {
                        BookedFinancePosting posting = setupBaseData(nextYearStart);
                        posting.setAmount(financeAccount.getSum());
                        posting.setFinanceAccount(financeAccount);
                        primoPostings.add(posting);
                    }
                    break;
                }
                case BALANCE_CHECK:
                case CURRENT_RESULT:
                case CATEGORY:
                case HEADLINE:
                case SUM: {
                    break;
                }
                default: {
                    throw new NullPointerException("All account types should be handled");
                }
            }
        }

        BookedFinancePosting year_end_posting = setupBaseData(nextYearStart);
        year_end_posting.setFinanceAccount(year_end_account);
        year_end_posting.setAmount(resultOfYear);
        primoPostings.add(year_end_posting);

        for (BookedFinancePosting posting : primoPostings) {
            postingService.saveBookedFinancePosting(posting);
        }

        fiscalYear.setFiscalYearStatus(FiscalYearStatus.LOCKED);
        save(fiscalYear);

        log.info("closed fiscal year " + fiscalYear + " with year result " + resultOfYear);

    }

    private BookedFinancePosting setupBaseData(Date date) {
        BookedFinancePosting bookedFinancePosting = new BookedFinancePosting();
        bookedFinancePosting.setBookedFinancePostingType(BookedFinancePostingType.PRIMO);
        bookedFinancePosting.setDate(date);
        bookedFinancePosting.setBookingNumber(0);
        bookedFinancePosting.setText(new ResourceModel("primo.posting").getObject());
        return bookedFinancePosting;
    }

    @Transactional
    public List<FiscalYear> findFiscalYearByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        log.debug("Finding all FiscalYear from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_BY_LEGAL_ENTITY, Integer.valueOf(first), Integer.valueOf(count), legalEntity);
        return list;
    }

    @Transactional
    public List<FiscalYear> findOpenFiscalYearByLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding open FiscalYear from legal entity from " + legalEntity.toString());
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_OPEN_BY_LEGAL_ENTITY, legalEntity, FiscalYearStatus.OPEN);
        return list;
    }

    @Transactional
    public List<FiscalYear> findOpenFiscalYearByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        log.debug("Finding open FiscalYear from legal entity from " + legalEntity.toString() + " first " + first + " count " + count);
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_OPEN_BY_LEGAL_ENTITY, Integer.valueOf(first), Integer.valueOf(count), legalEntity, FiscalYearStatus.OPEN);
        return list;
    }

    @Transactional
    public FiscalYear findNextFiscalYearByLegalEntity(LegalEntity legalEntity, Date date) {
        log.debug("Finding next FiscalYear from legal entity from " + legalEntity.toString());
        FiscalYear fiscalYear = fiscalYearDAO.findByNamedQueryUnique(FiscalYear.QUERY_FIND_NEXT_OPEN_BY_LEGAL_ENTITY, legalEntity, date, FiscalYearStatus.OPEN);
        return fiscalYear;
    }

    @Transactional
    public List<FiscalYear> findFiscalYearByLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding all FiscalYear from legal entity from " + legalEntity.toString());
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }

    @Transactional
    public List<FiscalYear> findLockedFiscalYearByLegalEntityAfterDate(LegalEntity legalEntity, Date date) {
        log.debug("Finding closed FiscalYear from legal entity from " + legalEntity.toString() + " after date" + date);
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_LOCKED_BY_LEGAL_ENTITY_AFTER_DATE, legalEntity, date, FiscalYearStatus.LOCKED);
        return list;
    }

    @Transactional
    public List<FiscalYear> findLockedFiscalYearByLegalEntityAfterDateSubList(LegalEntity legalEntity, Date date, int first, int count) {
        log.debug("Finding closed FiscalYear from legal entity from " + legalEntity.toString() + " after date" + date + " first " + first + " count " + count);
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_LOCKED_BY_LEGAL_ENTITY_AFTER_DATE, Integer.valueOf(first), Integer.valueOf(count), legalEntity, date, FiscalYearStatus.LOCKED);
        return list;
    }

    @Transactional
    public List<FiscalYear> findOpenFiscalYearByLegalEntityBeforeDate(LegalEntity legalEntity, Date date) {
        log.debug("Finding open FiscalYear from legal entity from " + legalEntity.toString() + " before date" + date);
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_OPEN_BY_LEGAL_ENTITY_BEFORE_DATE, legalEntity, date, FiscalYearStatus.OPEN);
        return list;
    }

    @Transactional
    public List<FiscalYear> findOpenFiscalYearByLegalEntityBeforeDateSubList(LegalEntity legalEntity, Date date, int first, int count) {
        log.debug("Finding open FiscalYear from legal entity from " + legalEntity.toString() + " before date" + date + " first " + first + " count " + count);
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_OPEN_BY_LEGAL_ENTITY_BEFORE_DATE, Integer.valueOf(first), Integer.valueOf(count), legalEntity, date, FiscalYearStatus.OPEN);
        return list;
    }

    @Transactional
    public FiscalYear findLastFiscalYearByLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding last FiscalYear from legal entity from " + legalEntity.toString());
        FiscalYear last = fiscalYearDAO.findByNamedQueryUnique(FiscalYear.QUERY_FIND_LAST_BY_LEGAL_ENTITY, legalEntity);
        return last;
    }


}

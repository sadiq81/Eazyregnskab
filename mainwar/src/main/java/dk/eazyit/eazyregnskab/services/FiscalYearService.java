package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.BookedFinancePostingDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.FiscalYearDAO;
import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.util.CalenderUtil;
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

    private Logger log = LoggerFactory.getLogger(LegalEntityService.class);

    @Autowired
    FiscalYearDAO fiscalYearDAO;
    @Autowired
    ReportService reportService;
    @Autowired
    FinanceAccountService financeAccountService;
    @Autowired
    BookedFinancePostingDAO bookedFinancePostingDAO;

    @Transactional
    public boolean save(FiscalYear fiscalYear) {

        if (fiscalYear.getId() != null && fiscalYear.getId() != 0) {

            //TODO on change check if year contains postings beyond changed dates
            fiscalYearDAO.save(fiscalYear);
        } else {
            fiscalYearDAO.save(fiscalYear);
        }

        return true;

    }

    @Transactional
    public boolean deleteFiscalYear(FiscalYear fiscalYear) {

        //TODO checks for postings
        fiscalYearDAO.delete(fiscalYear);
        return true;
    }

    public void closeFiscalYear(FiscalYear fiscalYear, Date nextYearStart) {

        if (CalenderUtil.add(fiscalYear.getEnd(), 0, 0, 1).compareTo(nextYearStart) != 0) {
            throw new NullPointerException("Next year must be 1 day after previous");
        }

        List<FinanceAccount> financeAccountsWithSum = reportService.getFinanceAccountsWithSum(fiscalYear);

        FinanceAccount year_end_account = financeAccountService.findSystemFinanceAccountByLegalEntity(fiscalYear.getLegalEntity(), FinanceAccountType.YEAR_END);

        Double resultOfYear = new Double(0);
        List<BookedFinancePosting> primoPostings = new ArrayList<BookedFinancePosting>();

        for (FinanceAccount financeAccount : financeAccountsWithSum) {

            switch (financeAccount.getFinanceAccountType()) {
                case PROFIT:
                case EXPENSE:
                case YEAR_END: {
                    resultOfYear = resultOfYear + financeAccount.getSum();
                    break;
                }
                case ASSET:
                case LIABILITY: {
                    BookedFinancePosting posting  = setupBaseData(nextYearStart);
                    posting.setAmount(financeAccount.getSum());
                    posting.setFinanceAccount(financeAccount);
                    primoPostings.add(posting);
                    break;
                }
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
        primoPostings.add(year_end_posting);

        fiscalYear.setFiscalYearStatus(FiscalYearStatus.LOCKED);
//        save(fiscalYear);

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
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<FiscalYear> findOpenFiscalYearByLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding open FiscalYear from legal entity from " + legalEntity.toString());
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_OPEN_BY_LEGAL_ENTITY, legalEntity, FiscalYearStatus.OPEN);
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
    public List<FiscalYear> findOpenFiscalYearByLegalEntityBeforeDate(LegalEntity legalEntity, Date date) {
        log.debug("Finding open FiscalYear from legal entity from " + legalEntity.toString() + " before date" + date);
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_OPEN_BY_LEGAL_ENTITY_BEFORE_DATE, legalEntity, date, FiscalYearStatus.OPEN);
        return list;
    }


}

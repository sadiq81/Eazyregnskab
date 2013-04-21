package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.*;
import dk.eazyit.eazyregnskab.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author
 */
@Service("financeAccountService")
public class FinanceAccountService {

    private Logger log = LoggerFactory.getLogger(LegalEntityService.class);

    @Autowired
    private FinanceAccountDAO financeAccountDAO;
    @Autowired
    private DraftFinancePostingDAO draftFinancePostingDAO;
    @Autowired
    private BookedFinancePostingDAO bookedFinancePostingDAO;
    @Autowired
    private VatTypeDAO vatTypeDAO;
    @Autowired
    private DailyLedgerDAO dailyLedgerDAO;

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntity(LegalEntity legalEntity) {
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntity(LegalEntity legalEntity) {
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }


//    ------------------------------------------------------------------------------------------------------------------------------

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<DraftFinancePosting> findFinancePostingByDailyLedgerSubList(DailyLedger dailyLedger, int first, int count) {
        List<DraftFinancePosting> list = draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, new Integer(first), new Integer(count), dailyLedger);
        return list;
    }

    @Transactional
    public List<VatType> findVatTypeByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        List<VatType> list = vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }


//    ------------------------------------------------------------------------------------------------------------------------------


//    ------------------------------------------------------------------------------------------------------------------------------


    @Transactional
    public int countFinanceAccountOfLegalEntity(LegalEntity legalEntity) {
        return financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional
    public int countDailyLedgerOfLegalEntity(LegalEntity legalEntity) {
        return dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional
    public int countFinancePostingOfDailyLedger(DailyLedger dailyLedger) {
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger).size();
    }

    @Transactional
    public int countVatTypesOfLegalEntity(LegalEntity legalEntity) {
        return vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity).size();
    }

//    ------------------------------------------------------------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<VatType> findAllVatTypesForLegalEntity(LegalEntity legalEntity) {
        return vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity);
    }

//    ------------------------------------------------------------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<DraftFinancePosting> findPostingsFromAccount(FinanceAccount financeAccount) {
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, financeAccount);
    }

    @Transactional(readOnly = true)
    public List<DraftFinancePosting> findPostingsFromDailyLedger(DailyLedger dailyLedger) {
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger);
    }

//    ------------------------------------------------------------------------------------------------------------------------------


    @Transactional(readOnly = true)
    public boolean isDeletingFinanceAccountAllowed(FinanceAccount financeAccount) {
        return findPostingsFromAccount(financeAccount).size() == 0;
    }

    @Transactional(readOnly = true)
    public boolean isDeletingDailyLedgerAllowed(DailyLedger dailyLedger, LegalEntity legalEntity) {
        return findPostingsFromDailyLedger(dailyLedger).size() == 0 &&
                dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size() > 1 &&
                draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger).size() == 0;
    }

    @Transactional(readOnly = true)
    public boolean isDeletingVatTypeAllowed(VatType vatType) {
        return (draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, vatType).size() == 0 &&
                bookedFinancePostingDAO.findByNamedQuery(BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, vatType).size() == 0);

    }

//    ------------------------------------------------------------------------------------------------------------------------------

    @Transactional
    public void deleteFinanceAccount(FinanceAccount financeAccount) {
        financeAccountDAO.delete(financeAccount);
    }

    @Transactional
    public void deleteDailyLedger(DailyLedger dailyLedger) {
        dailyLedgerDAO.delete(dailyLedger);
    }

    @Transactional
    public void deleteFinancePosting(DraftFinancePosting draftFinancePosting) {
        draftFinancePostingDAO.delete(draftFinancePosting);
    }

    @Transactional
    public void deleteVatType(VatType vatType) {
        vatTypeDAO.delete(vatType);
    }

//    ------------------------------------------------------------------------------------------------------------------------------


    @Transactional
    public void saveFinanceAccount(FinanceAccount financeAccount, LegalEntity legalEntity) {
        if (financeAccount.getId() == 0) {
            financeAccount.setLegalEntity(legalEntity);
            financeAccountDAO.create(financeAccount);
        } else {
            financeAccountDAO.save(financeAccount);
        }
    }

    @Transactional
    public void saveDailyLedger(DailyLedger dailyLedger, LegalEntity legalEntity) {
        if (dailyLedger.getId() == 0) {
            dailyLedger.setLegalEntity(legalEntity);
            dailyLedgerDAO.create(dailyLedger);
        } else {
            dailyLedgerDAO.save(dailyLedger);
        }
    }

    @Transactional
    public void saveDraftFinancePosting(DraftFinancePosting draftFinancePosting) {
        draftFinancePostingDAO.save(draftFinancePosting);
    }

    @Transactional
    public void saveVatType(VatType vatType, LegalEntity legalEntity) {
        if (vatType.getId() == 0) {
            vatType.setLegalEntity(legalEntity);
            vatTypeDAO.create(vatType);
        }
        vatTypeDAO.save(vatType);
    }

//    ------------------------------------------------------------------------------------------------------------------------------

    @Transactional
    public FinanceAccount findFinanceAccountById(long l) {
        return financeAccountDAO.findById(l);
    }


//    ------------------------------------------------------------------------------------------------------------------------------

}

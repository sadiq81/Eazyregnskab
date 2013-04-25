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

    private Logger LOG = LoggerFactory.getLogger(FinanceAccountService.class);

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
        LOG.debug("Finding all FinanceAccount from legal entity " + legalEntity.toString());
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all DailyLedger from legal entity " + legalEntity.toString());
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }

    @Transactional(readOnly = true)
    public List<VatType> findAllVatTypesForLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all VatType from legal entity " + legalEntity.toString());
        return vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity);
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


//    ------------------------------------------------------------------------------------------------------------------------------

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        LOG.debug("Finding all FinanceAccount from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        LOG.debug("Finding all DailyLedger from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<DraftFinancePosting> findFinancePostingByDailyLedgerSubList(DailyLedger dailyLedger, int first, int count) {
        LOG.debug("Finding all DraftFinancePosting from DailyLedger starting with " + first + " to  " + count + " from " + dailyLedger.toString());
        List<DraftFinancePosting> list = draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, new Integer(first), new Integer(count), dailyLedger);
        return list;
    }

    @Transactional
    public List<VatType> findVatTypeByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        LOG.debug("Finding all VatType from legalEntity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<VatType> list = vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }


//    ------------------------------------------------------------------------------------------------------------------------------

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntitySubListSortBy(LegalEntity legalEntity, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all FinanceAccount from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), sortProperty, Ascending, legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntitySubListSortBy(LegalEntity legalEntity, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all DailyLedger from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), sortProperty, Ascending, legalEntity);
        return list;
    }

    @Transactional
    public List<DraftFinancePosting> findFinancePostingByDailyLedgerSubListSortBy(DailyLedger dailyLedger, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all DraftFinancePosting from DailyLedger starting with " + first + " to  " + count + " from " + dailyLedger.toString());
        List<DraftFinancePosting> list = draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, new Integer(first), new Integer(count), sortProperty, Ascending, dailyLedger);
        return list;
    }

    @Transactional
    public List<VatType> findVatTypeByLegalEntitySubListSortBy(LegalEntity legalEntity, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all VatType from legalEntity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<VatType> list = vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), sortProperty, Ascending, legalEntity);
        return list;
    }

//    ------------------------------------------------------------------------------------------------------------------------------


    @Transactional
    public int countFinanceAccountOfLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Couting all financeAccount from legalEntity " + legalEntity.toString());
        return financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional
    public int countDailyLedgerOfLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Couting all DailyLedger from legalEntity " + legalEntity.toString());
        return dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional
    public int countFinancePostingOfDailyLedger(DailyLedger dailyLedger) {
        LOG.debug("Couting all DraftFinancePosting from dailyLedger " + dailyLedger.toString());
        return draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger).size();
    }

    @Transactional
    public int countVatTypesOfLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Couting all VatType from legalEntity " + legalEntity.toString());
        return vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity).size();
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
    public boolean deleteFinanceAccount(FinanceAccount financeAccount) {
        if (isDeletingFinanceAccountAllowed(financeAccount)) {
            financeAccountDAO.delete(financeAccount);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean deleteDailyLedger(DailyLedger dailyLedger) {
        if (isDeletingDailyLedgerAllowed(dailyLedger, dailyLedger.getLegalEntity())) {
            dailyLedgerDAO.delete(dailyLedger);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void deleteFinancePosting(DraftFinancePosting draftFinancePosting) {
        draftFinancePostingDAO.delete(draftFinancePosting);
    }

    @Transactional
    public boolean deleteVatType(VatType vatType) {
        if (isDeletingVatTypeAllowed(vatType)) {
            vatTypeDAO.delete(vatType);
            return true;
        } else {
            return false;
        }
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

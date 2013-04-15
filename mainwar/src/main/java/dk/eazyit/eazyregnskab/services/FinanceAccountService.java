package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.DailyLedgerDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.FinanceAccountDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.FinancePostingDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.VatTypeDAO;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinancePosting;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.VatType;
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
    private FinancePostingDAO financePostingDAO;
    @Autowired
    private VatTypeDAO vatTypeDAO;
    @Autowired
    private DailyLedgerDAO dailyLedgerDAO;

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding all finance account from " + legalEntity.getName());
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        log.debug("Finding sublist size " + count + " of finance account from " + legalEntity.getName() + " starting from " + first);
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntitySubListOrderBy(LegalEntity legalEntity, int first, int count, String orderProperty, Boolean ascending) {
        log.debug("Finding sublist size " + count + " of finance account from " + legalEntity.getName() + " starting from " + first);

        List<FinanceAccount> list = financeAccountDAO.findByLegalEntityAndSortOrder(legalEntity, new Integer(first), new Integer(count), orderProperty, ascending);
        return list;
    }

    @Transactional
    public int countFinanceAccountOfLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding all finance account from " + legalEntity.getName());
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setLegalEntity(legalEntity);
        return financeAccountDAO.countByExample(financeAccount);
    }

    @Transactional(readOnly = true)
    public List<VatType> findAllVatTypesForLegalEntity(LegalEntity legalEntity) {
        return vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity);
    }

    @Transactional(readOnly = true)
    public List<FinancePosting> findPostingsFromAccount(FinanceAccount financeAccount) {
        return financePostingDAO.findByNamedQuery(FinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, financeAccount);
    }

    @Transactional(readOnly = true)
    public boolean isDeletingAllowed(FinanceAccount financeAccount) {
        return findPostingsFromAccount(financeAccount).size() == 0;
    }

    @Transactional
    public void deleteFinanceAccount(FinanceAccount financeAccount) {
        financeAccountDAO.delete(financeAccount);
    }

    @Transactional
    public void saveFinanceAccount(FinanceAccount financeAccount, LegalEntity legalEntity) {
        if (financeAccount.getId() == 0) {
            financeAccount.setLegalEntity(legalEntity);
            financeAccountDAO.create(financeAccount);
        } else {
            financeAccountDAO.save(financeAccount);
        }


    }
}

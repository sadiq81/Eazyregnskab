package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.FinanceAccountDAO;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private PostingService postingService;

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all FinanceAccount from legal entity " + legalEntity.toString());
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }

    @Transactional
    public FinanceAccount findSystemFinanceAccountByLegalEntity(LegalEntity legalEntity, FinanceAccountType financeAccountType) {
        LOG.debug("Finding system FinanceAccount from legal entity " + legalEntity.toString() + " with type " + financeAccountType);
        FinanceAccount account = financeAccountDAO.findByNamedQueryUnique(FinanceAccount.QUERY_FIND_SYSTEM_ACCOUNT_BY_LEGAL_ENTITY, legalEntity, financeAccountType);
        return account;
    }

    @Transactional
    public FinanceAccount findLowestFinanceAccountByLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding lowest FinanceAccount from legal entity " + legalEntity.toString());
        FinanceAccount lowest = financeAccountDAO.findByNamedQueryUnique(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_LOWEST, legalEntity);
        return lowest;
    }

    @Transactional
    public FinanceAccount findHighestFinanceAccountByLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding highest FinanceAccount from legal entity " + legalEntity.toString());
        FinanceAccount highest = financeAccountDAO.findByNamedQueryUnique(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_HIGHEST, legalEntity);
        return highest;
    }

    public List<FinanceAccount> findFinanceAccountByLegalEntityFromAccountToAccount(LegalEntity legalEntity, FinanceAccount fromAccount, FinanceAccount toAccount) {
        LOG.debug("Finding all FinanceAccount from legal entity " + legalEntity.toString() + " from account " + fromAccount + " to account " + toAccount);
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_AND_FROM_ACCOUNT_TO_ACCOUNT,
                legalEntity, fromAccount.getAccountNumber(), toAccount.getAccountNumber());
        return list;
    }

    public List<FinanceAccount> findBookableFinanceAccountByLegalEntityFromAccountToAccount(LegalEntity legalEntity, FinanceAccount fromAccount, FinanceAccount toAccount) {
        LOG.debug("Finding all bookable FinanceAccount from legal entity " + legalEntity.toString() + " from account " + fromAccount + " to account " + toAccount);
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BOOKABLE_BY_LEGAL_ENTITY_AND_FROM_ACCOUNT_TO_ACCOUNT,
                legalEntity, fromAccount.getAccountNumber(), toAccount.getAccountNumber());
        return list;
    }

    @Transactional
    public List<FinanceAccount> findBookableFinanceAccountByLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all bookable FinanceAccount from legal entity " + legalEntity.toString());
        List<FinanceAccount> temp = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        List<FinanceAccount> list = new ArrayList<FinanceAccount>();
        for (FinanceAccount financeAccount : temp) {
            if (financeAccount.isBookable()) {
                list.add(financeAccount);
            }
        }
        return list;
    }

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        LOG.debug("Finding all FinanceAccount from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, Integer.valueOf(first), Integer.valueOf(count), legalEntity);
        return list;
    }

    @Transactional
    public List<FinanceAccount> findFinanceAccountByLegalEntitySubListSortBy(LegalEntity legalEntity, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all FinanceAccount from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString() + " sorted by " + sortProperty);
        List<FinanceAccount> list = financeAccountDAO.findByNamedQuerySorted(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, Integer.valueOf(first), Integer.valueOf(count), sortProperty, Ascending, legalEntity);
        return list;
    }

    @Transactional
    public int countFinanceAccountOfLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Counting all financeAccount from legalEntity " + legalEntity.toString());
        return financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional
    public boolean isDeletingFinanceAccountAllowed(FinanceAccount financeAccount) {
        return !financeAccount.isInUse() && postingService.findDraftPostingsFromAccountSubList(financeAccount, 0, 1).size() == 0;
    }

    @Transactional
    public void deleteFinanceAccount(FinanceAccount financeAccount) {
        financeAccountDAO.delete(financeAccount);
    }

    @Transactional
    public void setFinanceAccountInUse(FinanceAccount financeAccountInUse) {
        LOG.debug("Setting financeAccount in use " + financeAccountInUse.toString());
        financeAccountInUse.setInUse(true);
        financeAccountDAO.save(financeAccountInUse);
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

    @Transactional
    public FinanceAccount findFinanceAccountById(long l) {
        LOG.debug("Finding financeAccount by id " + l);
        return financeAccountDAO.findById(l);
    }


    @Transactional
    public FinanceAccount findFinanceAccountByLegalEntityAndNumber(LegalEntity currentLegalEntity, Integer number) {
        LOG.debug("Finding financeAccount by legal entity  " + currentLegalEntity + " and number " + number);
        return financeAccountDAO.findByNamedQueryUnique(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_AND_ACCOUNT_NUMBER, currentLegalEntity, number);
    }
}

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
 * @author Trifork
 */
@Service("legalEntityService")
public class LegalEntityService {

    private Logger log = LoggerFactory.getLogger(LegalEntityService.class);

    @Autowired
    private LegalEntityDAO legalEntityDAO;
    @Autowired
    private LegalEntityAccessDAO legalEntityAccessDAO;
    @Autowired
    private DailyLedgerDAO dailyLedgerDAO;
    @Autowired
    private FinancePostingDAO financePostingDAO;
    @Autowired
    private FinanceAccountDAO financeAccountDAO;

    @Transactional
    public LegalEntity createLegalEntity(AppUser appUser, LegalEntity legalEntity) {
        log.debug("Creating new Legal Entity " + legalEntity);
        legalEntityDAO.create(legalEntity);
        legalEntityAccessDAO.create(new LegalEntityAccess(appUser, legalEntity));
        dailyLedgerDAO.create(new DailyLedger("Start", legalEntity));
        return legalEntity;
    }

    @Transactional
    public LegalEntity saveLegalEntity(AppUser appUser, LegalEntity legalEntity) {
        log.debug("Saving Legal Entity " + legalEntity);
        legalEntityDAO.save(legalEntity);
        return legalEntity;
    }

    @Transactional(readOnly = true)
    public LegalEntity findLegalEntityById(Long id) {
        log.debug("Finding Legal Entity with id " + id);
        return legalEntityDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<LegalEntity> findLegalEntityByUser(AppUser appUser) {
        log.debug("Finding all Legal Entity accessible by " + appUser.getUsername());
        List<LegalEntity> list = legalEntityDAO.findByNamedQuery(LegalEntity.QUERY_FIND_LEGAL_ENTITY_BY_USER, appUser);
        return list;
    }

    @Transactional()
    public List<LegalEntityAccess> findLegalEntityAccessByLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding all Legal Entity access to " + legalEntity.toString());
        return legalEntityAccessDAO.findByNamedQuery(LegalEntityAccess.QUERY_FIND_LEGAL_ENTITY_ACCESS_BY_LEGAL_ENTITY, legalEntity);
    }

    //TODO check for financepostings and ask for confirm dialog
    @Transactional
    public void deleteLegalEntity(AppUser appUser, LegalEntity legalEntity) {
        if (isDeletingAllowed(appUser, legalEntity)) {
            log.debug("Deleting legal entity " + legalEntity.toString());
            for (LegalEntityAccess legalEntityAccess : findLegalEntityAccessByLegalEntity(legalEntity)) {
                legalEntityAccessDAO.delete(legalEntityAccess);
            }
            for (DailyLedger dailyLedger : dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity)) {
                for (FinancePosting financePosting : financePostingDAO.findByNamedQuery(FinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger)) {
                    financePostingDAO.delete(financePosting);
                }
                dailyLedgerDAO.delete(dailyLedger);

            }
            for (FinanceAccount financeAccount : financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity)) {
                for (FinancePosting financePosting : financePostingDAO.findByNamedQuery(FinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, financeAccount)) {
                    financePostingDAO.delete(financePosting);
                }
                financeAccountDAO.delete(financeAccount);
            }
            legalEntityDAO.delete(legalEntity);
        } else {
            log.warn("Not able to delete legal entity " + legalEntity.toString() + " because it was the last");
        }
    }

    //TODO check for finance postings
    @Transactional(readOnly = true)
    public boolean isDeletingAllowed(AppUser appUser, LegalEntity legalEntity) {
        if (findLegalEntityByUser(appUser).size() <= 1) {
            return false;
        } else {
            return true;
        }
    }


}

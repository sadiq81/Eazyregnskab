package dk.eazyit.eazyregnskab.services;

/**
 * @author
 */

import dk.eazyit.eazyregnskab.dao.interfaces.DailyLedgerDAO;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("financeAccountService")
public class DailyLedgerService {

    private Logger LOG = LoggerFactory.getLogger(DailyLedgerService.class);

    @Autowired
    FinanceAccountService financeAccountService;

    @Autowired
    private DailyLedgerDAO dailyLedgerDAO;


    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all DailyLedger from legal entity " + legalEntity.toString());
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        LOG.debug("Finding all DailyLedger from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntitySubListSortBy(LegalEntity legalEntity, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all DailyLedger from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), sortProperty, Ascending, legalEntity);
        return list;
    }

    @Transactional
    public int countDailyLedgerOfLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Couting all DailyLedger from legalEntity " + legalEntity.toString());
        return dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional(readOnly = true)
    public boolean isDeletingDailyLedgerAllowed(DailyLedger dailyLedger, LegalEntity legalEntity) {
        return financeAccountService.findPostingsFromDailyLedger(dailyLedger).size() == 0 &&
                dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size() > 1;
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
    public void saveDailyLedger(DailyLedger dailyLedger, LegalEntity legalEntity) {
        if (dailyLedger.getId() == 0) {
            dailyLedger.setLegalEntity(legalEntity);
            dailyLedgerDAO.create(dailyLedger);
        } else {
            dailyLedgerDAO.save(dailyLedger);
        }
    }

    public DailyLedger findDailyLedgerByLegalEntityAndName(LegalEntity currentLegalEntity, String name) {
        return dailyLedgerDAO.findByNamedQueryUnique(DailyLedger.QUERY_FIND_BY_NAME_AND_LEGAL_ENTITY, name, currentLegalEntity);
    }


}

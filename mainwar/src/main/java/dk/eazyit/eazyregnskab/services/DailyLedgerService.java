package dk.eazyit.eazyregnskab.services;

/**
 * @author
 */

import dk.eazyit.eazyregnskab.dao.interfaces.DailyLedgerDAO;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("dailyLedgerService")
public class DailyLedgerService {

    private Logger LOG = LoggerFactory.getLogger(DailyLedgerService.class);

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private DailyLedgerDAO dailyLedgerDAO;

    @Autowired
    private PostingService postingService;


    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all DailyLedger from legal entity " + legalEntity.toString());
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        LOG.debug("Finding all DailyLedger from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, Integer.valueOf(first), Integer.valueOf(count), legalEntity);
        return list;
    }

    @Transactional
    public List<DailyLedger> findDailyLedgerByLegalEntitySubListSortBy(LegalEntity legalEntity, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all DailyLedger from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString() + " sorted by " + sortProperty);
        List<DailyLedger> list = dailyLedgerDAO.findByNamedQuerySorted(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, Integer.valueOf(first), Integer.valueOf(count), sortProperty, Ascending, legalEntity);
        return list;
    }

    @Transactional
    public int countDailyLedgerOfLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Couting all DailyLedger from legalEntity " + legalEntity.toString());
        return dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional(readOnly = true)
    public boolean isDeletingDailyLedgerAllowed(DailyLedger dailyLedger) {
        return postingService.findDraftPostingByDailyLedgerSubList(dailyLedger, 0, 1).size() == 0 &&
                findDailyLedgerByLegalEntitySubList(dailyLedger.getLegalEntity(), 0, 2).size() > 1;
    }

    @Transactional
    public void deleteDailyLedger(DailyLedger dailyLedger) {
        dailyLedgerDAO.delete(dailyLedger);
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
    public DailyLedger findDailyLedgerByLegalEntityAndName(LegalEntity currentLegalEntity, String name) {
        LOG.debug("Find DailyLedger from legalEntity " + currentLegalEntity.toString() + " and by name " + name);
        return dailyLedgerDAO.findByNamedQueryUnique(DailyLedger.QUERY_FIND_BY_NAME_AND_LEGAL_ENTITY, name, currentLegalEntity);
    }

    @Transactional
    public Double checkBalanceOfDailyLedger(DailyLedger dailyLedger) {
        LOG.debug("Checking balance ofDailyLedger " + dailyLedger);
        List<DraftFinancePosting> list = postingService.findDraftPostingsFromDailyLedger(dailyLedger);

        Double sum = new Double(0);
        for (DraftFinancePosting draftFinancePosting : list) {
            if (draftFinancePosting.getFinanceAccount() == null || draftFinancePosting.getReverseFinanceAccount() == null) {
                sum += draftFinancePosting.getAmount();
            }
        }
        return sum;
    }


}

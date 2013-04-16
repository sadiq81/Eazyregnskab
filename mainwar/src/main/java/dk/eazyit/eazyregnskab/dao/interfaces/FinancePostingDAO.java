package dk.eazyit.eazyregnskab.dao.interfaces;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.FinancePosting;

import java.util.List;

/**
 * @author EazyIT
 */
public interface FinancePostingDAO extends GenericDAO<FinancePosting, Long> {

    List<FinancePosting> findByDailyLedger(DailyLedger dailyLedger, int first, int count);

    List<FinancePosting> findByDailyLedgerAndSortOrder(DailyLedger dailyLedger, int first, int count, String orderProperty, Boolean ascending);


}

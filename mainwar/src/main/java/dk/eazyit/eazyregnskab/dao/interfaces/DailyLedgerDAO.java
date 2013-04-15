package dk.eazyit.eazyregnskab.dao.interfaces;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;

import java.util.List;

/**
 * @author EazyIT
 */
public interface DailyLedgerDAO extends GenericDAO<DailyLedger, Long>{

    List<DailyLedger> findByLegalEntity(LegalEntity legalEntity, int first, int count, String orderProperty, Boolean ascending);

    List<DailyLedger> findByLegalEntityAndSortOrder(LegalEntity legalEntity, int first, int count, String orderProperty, Boolean ascending);


}

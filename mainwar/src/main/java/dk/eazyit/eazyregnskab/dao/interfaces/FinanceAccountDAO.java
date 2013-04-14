package dk.eazyit.eazyregnskab.dao.interfaces;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.LegalEntity;

import java.util.List;

/**
 * @author EazyIT
 */
public interface FinanceAccountDAO extends GenericDAO<FinanceAccount, Long>{

    List<FinanceAccount> findByLegalEntityAndSortOrder(LegalEntity legalEntity, int first, int count, String orderProperty, Boolean ascending);
}

package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.FinanceAccountDAO;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import org.springframework.stereotype.Repository;

/**
 * @author EazyIT
 */
@Repository
public class FinanceAccountDAOImpl extends GenericDAOImpl<FinanceAccount, Long> implements FinanceAccountDAO {


}

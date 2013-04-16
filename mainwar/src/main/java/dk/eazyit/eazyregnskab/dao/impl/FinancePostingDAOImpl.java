package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.FinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.FinancePosting;
import org.springframework.stereotype.Repository;

/**
 * @author EazyIT
 */
@Repository
public class FinancePostingDAOImpl extends GenericDAOImpl<FinancePosting, Long> implements FinancePostingDAO {


}

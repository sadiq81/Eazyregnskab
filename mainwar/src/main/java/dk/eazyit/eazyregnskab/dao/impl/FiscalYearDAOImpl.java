package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.FiscalYearDAO;
import dk.eazyit.eazyregnskab.domain.FiscalYear;
import org.springframework.stereotype.Repository;

/**
 * @author EazyIT
 */
@Repository
public class FiscalYearDAOImpl extends GenericDAOImpl<FiscalYear, Long> implements FiscalYearDAO {

}

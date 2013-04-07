package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityDAO;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import org.springframework.stereotype.Repository;

/**
 * @author EazyIT
 */
@Repository
public class LegalEntityDAOImpl extends GenericDAOImpl<LegalEntity, Long> implements LegalEntityDAO {

}

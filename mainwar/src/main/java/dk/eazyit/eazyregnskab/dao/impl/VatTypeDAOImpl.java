package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.VatTypeDAO;
import dk.eazyit.eazyregnskab.domain.VatType;
import org.springframework.stereotype.Repository;

/**
 * @author EazyIT
 */
@Repository
public class VatTypeDAOImpl extends GenericDAOImpl<VatType, Long> implements VatTypeDAO {

}

package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import org.springframework.stereotype.Repository;

/**
 * @author Trifork
 */
@Repository
public class AppUserDAOImpl extends GenericDAOImpl<AppUser, Long> implements AppUserDAO {

}

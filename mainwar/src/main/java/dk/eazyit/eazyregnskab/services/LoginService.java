package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.AppUserRoleDAO;
import dk.eazyit.eazyregnskab.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Trifork
 */
@Service
public class LoginService {

    private Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    ShaPasswordEncoder shaPasswordEncoder;

    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private AppUserRoleDAO appUserRoleDAO;
    @Autowired
    private LegalEntityService legalEntityService;
    @Autowired
    private FinanceAccountService financeAccountService;


    @Transactional
    public void createUser(String username, String password) {
        AppUser appUser;
        appUserDAO.create(appUser = new AppUser(username, shaPasswordEncoder.encodePassword(password, username), true));
        appUserRoleDAO.create(new AppUserRole(appUser, Authority.ROLE_USER));
        legalEntityService.createLegalEntity(appUser);

    }

    @Transactional(readOnly = true)
    public AppUser findAppUserByUsername(String username) {
        AppUser appUser = appUserDAO.findByNamedQueryUnique(AppUser.QUERY_FIND_BY_USER_NAME, username);

        return appUser;
    }

}

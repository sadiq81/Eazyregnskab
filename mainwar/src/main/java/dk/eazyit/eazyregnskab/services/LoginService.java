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


    @Transactional
    public void createUser(String username, String password) {
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(shaPasswordEncoder.encodePassword(password, username));
        appUser.setEnabled(true);
        appUserDAO.create(appUser);
        AppUserRole appUserRole = new AppUserRole(appUser, Authority.ROLE_USER);
        appUserRoleDAO.create(appUserRole);

        legalEntityService.createLegalEntity(appUser, new LegalEntity("Start",null,null,null, Country.DK, MoneyCurrency.DKK));


    }

    @Transactional(readOnly = true)
    public AppUser findAppUserByUsername(String username) {
        AppUser appUser = appUserDAO.findByNamedQueryUnique(AppUser.QUERY_FIND_BY_USER_NAME, username);

        return appUser;
    }

}

package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.AppUserRoleDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.AppUserRole;
import dk.eazyit.eazyregnskab.domain.Authority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    @Autowired
    MailService mailService;


    @Transactional
    public void createUser(String username, String password, String email) {
        AppUser appUser = new AppUser(username, shaPasswordEncoder.encodePassword(password, username), false, email, UUID.randomUUID().toString());
        mailService.SendConfirmationEmail(appUser.getEmail(), appUser.getVerificationUUID());
        appUserDAO.create(appUser);
        appUserRoleDAO.create(new AppUserRole(appUser, Authority.ROLE_USER));
        legalEntityService.createLegalEntity(appUser);

    }



    @Transactional(readOnly = true)
    public AppUser findAppUserByUsername(String username) {
        AppUser appUser = appUserDAO.findByNamedQueryUnique(AppUser.QUERY_FIND_BY_USER_NAME, username);
        return appUser;
    }

}

package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.AppUserRoleDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.AppUserRole;
import dk.eazyit.eazyregnskab.security.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Trifork
 */
@Service
public class LoginService {

    private Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private AppUserRoleDAO appUserRoleDAO;
    @Autowired
    private LegalEntityService legalEntityService;
    @Autowired
    private FinanceAccountService financeAccountService;

    @Qualifier("MailService")
    @Autowired
    MailService mailService;


    @Transactional
    public void createUser(String username, String password, String email) {
        AppUser appUser = new AppUser(username, PasswordEncoder.getInstance().encode(password, username), false, email, UUID.randomUUID().toString());
        mailService.SendConfirmationEmail(appUser.getEmail(), appUser.getVerificationUUID());
        appUserDAO.create(appUser);
        appUserRoleDAO.create(new AppUserRole(appUser, "USER"));
        log.info("Created account for " + username);

    }

    @Transactional
    public boolean activeUser(String username, String password, String UUID) {
        AppUser appUser = findAppUserByUsername(username);
        String pass2 = PasswordEncoder.getInstance().encode(password, username);
        if (appUser != null && appUser.getPassword().equals(pass2) && appUser.getVerificationUUID().equals(UUID)) {
            appUser.setEnabled(true);
            appUser.setVerificationUUID(null);
            legalEntityService.createLegalEntity(appUser);
            appUserDAO.save(appUser);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public String getUserRolesAsString(AppUser appUser) {

        StringBuilder roles = new StringBuilder();
        for (AppUserRole role : appUserRoleDAO.findByNamedQuery(AppUserRole.QUERY_FIND_BY_USER, appUser)) {
            if (roles.length() > 0) {
                roles.append(",");
            }
            roles.append(role.getRole());
        }
        return roles.toString();
    }

    @Transactional
    public List<AppUserRole> getUserRoles(AppUser appUser) {
        return appUserRoleDAO.findByNamedQuery(AppUserRole.QUERY_FIND_BY_USER, appUser);
    }


    @Transactional(readOnly = true)
    public AppUser findAppUserByUsername(String username) {
        AppUser appUser = appUserDAO.findByNamedQueryUnique(AppUser.QUERY_FIND_BY_USER_NAME, username);
        return appUser;
    }

    @Transactional(readOnly = true)
    public AppUser findAppUserByEmail(String email) {
        AppUser appUser = appUserDAO.findByNamedQueryUnique(AppUser.QUERY_FIND_BY_EMAIL, email);
        return appUser;
    }

    @Transactional
    public AppUser saveUser(AppUser currentUser) {
        return appUserDAO.save(currentUser);
    }
}

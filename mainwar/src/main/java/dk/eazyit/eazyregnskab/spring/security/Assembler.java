package dk.eazyit.eazyregnskab.spring.security;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserRoleDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.AppUserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Trifork
 */
@Service("assembler")
public class Assembler {

    private static final Logger LOG = LoggerFactory.getLogger(Assembler.class);


    @Autowired
    AppUserRoleDAO appUserRoleDAO;

    @Transactional(readOnly = true)
    User buildUserFromUserEntity(AppUser appUser) {

        LOG.debug("Assembling Spring user from AppUser " + appUser.toString());

        String username = appUser.getUsername();
        String password = appUser.getPassword();
        boolean enabled = appUser.isActive();
        boolean accountNonExpired = appUser.isActive();
        boolean credentialsNonExpired = appUser.isActive();
        boolean accountNonLocked = appUser.isActive();

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (AppUserRole role : appUserRoleDAO.findByNamedQuery(AppUserRole.QUERY_FIND_BY_USER, appUser)) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority().name()));
        }

        User user = new User(username, password, enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

        LOG.debug("Spring user assembled " + user.toString());
        return user;
    }
}

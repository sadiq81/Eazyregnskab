package dk.eazyit.eazyregnskab.spring.security;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.AppUserRole;
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

    @Transactional(readOnly = true)
    User buildUserFromUserEntity(AppUser appUser) {

        String username = appUser.getUsername();
        String password = appUser.getPassword();
        boolean enabled = appUser.isActive();
        boolean accountNonExpired = appUser.isActive();
        boolean credentialsNonExpired = appUser.isActive();
        boolean accountNonLocked = appUser.isActive();

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (AppUserRole role : appUser.getAppUserRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority().name()));
        }

        User user = new User(username, password, enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        return user;
    }
}

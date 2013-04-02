package dk.eazyit.eazyregnskab.spring.security;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Trifork
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private Assembler assembler;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userDetails = null;
        AppUser appUser = appUserDAO.findByNamedQueryUnique(AppUser.QUERY_FIND_BY_USER_NAME, username);
        if (appUser == null)
            throw new UsernameNotFoundException("user not found");
        return assembler.buildUserFromUserEntity(appUser);
    }


}

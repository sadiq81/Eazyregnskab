package dk.eazyit.eazyregnskab.services.security;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Trifork
 */
@Service("UserDetailsService")
public class UserServiceImpl implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private Assembler assembler;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LOG.debug("loading user by username " + username);

        UserDetails userDetails = null;
        AppUser appUser = loginService.findAppUserByUsername(username);
        if (appUser == null)
            throw new UsernameNotFoundException("user not found");
        return assembler.buildUserFromUserEntity(appUser);
    }


}

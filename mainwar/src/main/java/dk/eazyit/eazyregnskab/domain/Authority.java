package dk.eazyit.eazyregnskab.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Trifork
 */
public enum Authority implements GrantedAuthority {

    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}

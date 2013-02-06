package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.domain.AppUser;

/**
 * @author Trifork
 */
public class EazyRegnskabSession {

    private static EazyRegnskabSession eazyRegnskabSession;
    private AppUser appUser;

    private EazyRegnskabSession() {
        appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("test");
        appUser.setPassword("test");
    }

    public EazyRegnskabSession getEazyRegnskabSession() {

        if (eazyRegnskabSession == null) {
            eazyRegnskabSession = new EazyRegnskabSession();
        }
        return eazyRegnskabSession;
    }

}

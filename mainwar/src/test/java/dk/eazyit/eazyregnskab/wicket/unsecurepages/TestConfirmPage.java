package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.AppUserRole;
import dk.eazyit.eazyregnskab.domain.Role;
import dk.eazyit.eazyregnskab.web.app.front.ConfirmPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

import java.util.HashSet;

/**
 * @author
 */
public class TestConfirmPage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        AppUser appUser = new AppUser("test2", "test2", false, "tommy@sadiq.dk", "veryLongId");
        appUserDAO.save(appUser);
        HashSet<AppUserRole> appUserRoles = new HashSet<AppUserRole>();
        AppUserRole appUserRole = new AppUserRole(appUser, Role.USER);
        appUserRoleDAO.save(appUserRole);
        appUserRoles.add(appUserRole);
        appUser.setAppUserRoles(appUserRoles);

        tester.startPage(ConfirmPage.class, new PageParameters().add("id", "veryLongId"));
        tester.assertRenderedPage(ConfirmPage.class);

        tester.assertComponent("feedback", NotificationPanel.class);
    }
}

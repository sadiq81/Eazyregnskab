package dk.eazyit.eazyregnskab.hibernate;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.LegalEntityAccess;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author EazyIT
 */
public class TestLoginService  extends TestBaseCase {

    @Test
    public void testLoginService()
    {
        loginService.createUser("test","test");
        AppUser user = loginService.findAppUserByUsername("test");
        Assert.assertTrue(user.getUsername().equals("test"));
        List<LegalEntityAccess> list = legalEntityAccessDAO.findAll();
        Assert.assertTrue(list.size() == 1);
        List<LegalEntity> list2 = legalEntityService.findLegalEntityByUser(user);
        Assert.assertTrue(list2.size() == 1);

    }

}

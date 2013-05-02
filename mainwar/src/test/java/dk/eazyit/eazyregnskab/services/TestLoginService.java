package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.domain.*;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * @author EazyIT
 */

public class TestLoginService extends TestBaseCase {

    @Test
    @Rollback(false)
    public void testCreateNewUserAndFindUser() {
        loginService.createUser("AppUser", "AppUser","tommy@sadiq.dk");
        AppUser user = loginService.findAppUserByUsername("AppUser");
        Assert.assertTrue(user.getUsername().equals("AppUser"));

        List<LegalEntity> legalEntityList = legalEntityService.findLegalEntityByUser(user);
        Assert.assertTrue(legalEntityList.size() == 1);
        LegalEntity legalEntity = legalEntityList.get(0);

        List<LegalEntityAccess> legalEntityAccessList = legalEntityService.findLegalEntityAccessByLegalEntity(legalEntity);
        Assert.assertTrue(legalEntityAccessList.size() == 1);
        Assert.assertTrue(user.equals(legalEntityAccessList.get(0).getAppUser()));

        List<DailyLedger> dailyLedgers = financeAccountService.findDailyLedgerByLegalEntity(legalEntity);
        Assert.assertTrue(dailyLedgers.size() == 1);

        List<FinanceAccount> financeAccountList = financeAccountService.findFinanceAccountByLegalEntity(legalEntity);
        Assert.assertTrue(financeAccountList.size() == 94);

    }
}

package dk.eazyit.eazyregnskab.hibernate;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * @author EazyIT
 */

public class TestLoginService  extends TestBaseCase {

    @Test
    @Rollback
    public void testLoginService()
    {
        loginService.createUser("AppUser","Password");
        AppUser user = loginService.findAppUserByUsername("AppUser");
        Assert.assertTrue(user.getUsername().equals("AppUser"));

        List<LegalEntity> legalEntityList = legalEntityService.findLegalEntityByUser(user);
        Assert.assertTrue(legalEntityList.size() == 1);
        LegalEntity legalEntity = legalEntityList.get(0);

        List<DailyLedger> dailyLedgers = financeAccountService.findDailyLedgerByLegalEntity(legalEntity);
        Assert.assertTrue(dailyLedgers.size() == 1);

        List<FinanceAccount> financeAccountList = financeAccountService.findFinanceAccountByLegalEntity(legalEntity);
        Assert.assertTrue(financeAccountList.size() == 94);

    }

}

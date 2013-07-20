package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class TestBookingService extends TestLoggedInPage {

    VatType vatIncoming;
    VatType vatOutgoing;
    VatType vatIncomingEU;
    VatType vatOutgoingEU;

    FinanceAccount sales;
    FinanceAccount salesWithOutVat;
    FinanceAccount salesWithEUVat;

    FinanceAccount purchases;
    FinanceAccount purchasesWithOutVat;
    FinanceAccount purchasesWithEUVat;

    FinanceAccount bank;

    FinanceAccount incomingVat;
    FinanceAccount outgoingVat;
    FinanceAccount incomingEUVat;
    FinanceAccount outgoingEUVat;


    @Before
    public void setUpVatAccounts() {

        financeAccountService.saveFinanceAccount(sales = new FinanceAccount("sales", 1000, FinanceAccountType.PROFIT, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(salesWithOutVat = new FinanceAccount("salesWithOutVat", 2000, FinanceAccountType.PROFIT, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(salesWithEUVat = new FinanceAccount("salesWithEUVat", 3000, FinanceAccountType.PROFIT, legalEntity), legalEntity);

        financeAccountService.saveFinanceAccount(purchases = new FinanceAccount("purchases", 2000, FinanceAccountType.EXPENSE, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(purchasesWithOutVat = new FinanceAccount("purchasesWithOutVat", 3000, FinanceAccountType.EXPENSE, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(purchasesWithEUVat = new FinanceAccount("purchasesWithEUVat", 4000, FinanceAccountType.EXPENSE, legalEntity), legalEntity);

        financeAccountService.saveFinanceAccount(bank = new FinanceAccount("bank", 5000, FinanceAccountType.ASSET, legalEntity), legalEntity);

        financeAccountService.saveFinanceAccount(incomingVat = new FinanceAccount("", 8000, FinanceAccountType.LIABILITY, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(outgoingVat = new FinanceAccount("", 8001, FinanceAccountType.LIABILITY, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(incomingEUVat = new FinanceAccount("", 8002, FinanceAccountType.LIABILITY, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(outgoingEUVat = new FinanceAccount("", 8003, FinanceAccountType.LIABILITY, legalEntity), legalEntity);

        financeAccountService.saveVatType(vatIncoming = new VatType("vatIncoming", 25D, legalEntity, incomingVat),legalEntity);
        financeAccountService.saveVatType(vatOutgoing = new VatType("vatOutgoing", 25D, legalEntity, outgoingVat),legalEntity);
        financeAccountService.saveVatType(vatIncomingEU = new VatType("vatIncomingEU", 25D, incomingVat, incomingEUVat, legalEntity),legalEntity);
        financeAccountService.saveVatType(vatOutgoingEU = new VatType("vatOutgoingEU", 25D, outgoingVat, outgoingEUVat, legalEntity),legalEntity);

        sales.setVatType(vatOutgoing);
        salesWithEUVat.setVatType(vatOutgoingEU);

        purchases.setVatType(vatIncomingEU);
        purchasesWithEUVat.setVatType(vatOutgoingEU);

    }

    @Test
    @Rollback()
    public void testSingleVatPosting() {

        DraftFinancePosting draft = createBaseDraftFinancePosting();
        draft.setFinanceAccount(sales);
        draft.setReverseFinanceAccount(bank);
        draft.setDailyLedger(dailyLedger);
        draft.setVatType(sales.getVatType());
        draftFinancePostingDAO.save(draft);

        bookingService.BookAll(dailyLedger, new BookingResult());

        List<BookedFinancePosting> list = bookedFinancePostingDAO.findAll();
        Assert.assertTrue(list.size() == 3);

    }

    @Test
    @Rollback()
    public void testDoubleVatPosting() {

    }

    @Test
    @Rollback()
    public void testSingleVatWithReversePosting() {

    }

    @Test
    @Rollback()
    public void testDoubleVatWithReversePosting() {

    }

    private DraftFinancePosting createBaseDraftFinancePosting() {

        DraftFinancePosting draftFinancePosting = new DraftFinancePosting();
        draftFinancePosting.setBookingNumber(1);
        draftFinancePosting.setDate(new Date());
        draftFinancePosting.setText("Test Booking");
        draftFinancePosting.setAmount(new Double(100));


        return draftFinancePosting;
    }


}

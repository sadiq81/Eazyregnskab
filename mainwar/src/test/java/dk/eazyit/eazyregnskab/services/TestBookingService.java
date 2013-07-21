package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
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

        financeAccountService.saveFinanceAccount(incomingVat = new FinanceAccount("incomingVat", 8000, FinanceAccountType.LIABILITY, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(outgoingVat = new FinanceAccount("outgoingVat", 8001, FinanceAccountType.LIABILITY, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(incomingEUVat = new FinanceAccount("incomingEUVat", 8002, FinanceAccountType.LIABILITY, legalEntity), legalEntity);
        financeAccountService.saveFinanceAccount(outgoingEUVat = new FinanceAccount("outgoingEUVat", 8003, FinanceAccountType.LIABILITY, legalEntity), legalEntity);

        financeAccountService.saveVatType(vatIncoming = new VatType("vatIncoming", 25D, legalEntity, incomingVat), legalEntity);
        financeAccountService.saveVatType(vatOutgoing = new VatType("vatOutgoing", 25D, legalEntity, outgoingVat), legalEntity);
        financeAccountService.saveVatType(vatIncomingEU = new VatType("vatIncomingEU", 25D, incomingVat, incomingEUVat, legalEntity), legalEntity);
        financeAccountService.saveVatType(vatOutgoingEU = new VatType("vatOutgoingEU", 25D, outgoingVat, outgoingEUVat, legalEntity), legalEntity);

        sales.setVatType(vatOutgoing);
        salesWithEUVat.setVatType(vatOutgoingEU);

        purchases.setVatType(vatIncoming);
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

        List<FinanceAccount> accountList = new ArrayList<FinanceAccount>();
        accountList.add(sales);
        accountList.add(bank);
        accountList.add(sales.getVatType().getFinanceAccount());

        List<BookedFinancePosting> list = bookedFinancePostingDAO.findAll();
        Assert.assertTrue(list.size() == 3);

        double amount = 0;
        for (BookedFinancePosting posting : list) {
            Assert.assertTrue(posting.getText().equals("Test Booking"));
            Assert.assertTrue(posting.getBookingNumber() == 1);
            accountList.remove(posting.getFinanceAccount());
            amount += posting.getAmount();
        }

        Assert.assertTrue(accountList.size() == 0);
        Assert.assertTrue(amount == 0);

    }

    @Test
    @Rollback()
    public void testDoubleVatPosting() {

        DraftFinancePosting draft = createBaseDraftFinancePosting();
        draft.setFinanceAccount(sales);
        draft.setReverseFinanceAccount(purchases);
        draft.setDailyLedger(dailyLedger);
        draft.setVatType(sales.getVatType());
        draft.setReverseVatType(purchases.getVatType());
        draftFinancePostingDAO.save(draft);

        bookingService.BookAll(dailyLedger, new BookingResult());

        List<FinanceAccount> accountList = new ArrayList<FinanceAccount>();
        accountList.add(sales);
        accountList.add(purchases);
        accountList.add(sales.getVatType().getFinanceAccount());
        accountList.add(purchases.getVatType().getFinanceAccount());

        List<BookedFinancePosting> list = bookedFinancePostingDAO.findAll();
        Assert.assertTrue(list.size() == 4);

        double amount = 0;
        for (BookedFinancePosting posting : list) {
            Assert.assertTrue(posting.getText().equals("Test Booking"));
            Assert.assertTrue(posting.getBookingNumber() == 1);
            accountList.remove(posting.getFinanceAccount());
            amount += posting.getAmount();
        }

        Assert.assertTrue(accountList.size() == 0);
        Assert.assertTrue(amount == 0);
    }

    @Test
    @Rollback()
    public void testSingleVatWithReversePosting() {

        DraftFinancePosting draft = createBaseDraftFinancePosting();
        draft.setFinanceAccount(salesWithEUVat);
        draft.setReverseFinanceAccount(bank);
        draft.setDailyLedger(dailyLedger);
        draft.setVatType(salesWithEUVat.getVatType());
        draftFinancePostingDAO.save(draft);

        bookingService.BookAll(dailyLedger, new BookingResult());

        List<FinanceAccount> accountList = new ArrayList<FinanceAccount>();
        accountList.add(salesWithEUVat);
        accountList.add(bank);
        accountList.add(salesWithEUVat.getVatType().getFinanceAccount());
        accountList.add(salesWithEUVat.getVatType().getFinanceAccountReverse());

        List<BookedFinancePosting> list = bookedFinancePostingDAO.findAll();
        Assert.assertTrue(list.size() == 4);

        double amount = 0;
        for (BookedFinancePosting posting : list) {
            Assert.assertTrue(posting.getText().equals("Test Booking"));
            Assert.assertTrue(posting.getBookingNumber() == 1);
            accountList.remove(posting.getFinanceAccount());
            amount += posting.getAmount();
        }

        Assert.assertTrue(accountList.size() == 0);
        Assert.assertTrue(amount == 0);
    }

    @Test
    @Rollback()
    public void testDoubleVatWithReversePosting() {
        DraftFinancePosting draft = createBaseDraftFinancePosting();
        draft.setFinanceAccount(salesWithEUVat);
        draft.setReverseFinanceAccount(purchasesWithEUVat);
        draft.setDailyLedger(dailyLedger);
        draft.setVatType(salesWithEUVat.getVatType());
        draft.setReverseVatType(purchasesWithEUVat.getVatType());
        draftFinancePostingDAO.save(draft);

        bookingService.BookAll(dailyLedger, new BookingResult());

        List<FinanceAccount> accountList = new ArrayList<FinanceAccount>();
        accountList.add(salesWithEUVat);
        accountList.add(purchasesWithEUVat);
        accountList.add(salesWithEUVat.getVatType().getFinanceAccount());
        accountList.add(salesWithEUVat.getVatType().getFinanceAccountReverse());
        accountList.add(purchasesWithEUVat.getVatType().getFinanceAccount());
        accountList.add(purchasesWithEUVat.getVatType().getFinanceAccountReverse());

        List<BookedFinancePosting> list = bookedFinancePostingDAO.findAll();
        Assert.assertTrue(list.size() == 6);

        double amount = 0;
        for (BookedFinancePosting posting : list) {
            Assert.assertTrue(posting.getText().equals("Test Booking"));
            Assert.assertTrue(posting.getBookingNumber() == 1);
            accountList.remove(posting.getFinanceAccount());
            amount += posting.getAmount();
        }

        Assert.assertTrue(accountList.size() == 0);
        Assert.assertTrue(amount == 0);
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

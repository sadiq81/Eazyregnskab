package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.util.comparetors.BookedFinancePostingDateComparator;
import dk.eazyit.eazyregnskab.util.comparetors.FinanceAccountNumberComparator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author
 */
@Service
public class ReportService {

    private Logger log = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    FinanceAccountService financeAccountService;
    @Autowired
    PostingService postingService;

    private static Double ZERO = new Double(0);

    @Transactional(readOnly = true)
    public List<FinanceAccount> getFinanceAccountsWithSum(FiscalYear fiscalYear) {

        log.debug("Getting financeaccountswith sum");
        List<FinanceAccount> financeAccountsList = financeAccountService.findFinanceAccountByLegalEntity(fiscalYear.getLegalEntity());
        List<BookedFinancePosting> financePostingList = postingService.findBookedPostingsFromLegalEntityFromDateToDate(fiscalYear.getLegalEntity(), fiscalYear.getStart(), fiscalYear.getEnd());

        HashMap<Integer, FinanceAccount> financeAccountHashMap = new HashMap<Integer, FinanceAccount>();
        //Arrange and set sum = 0
        for (FinanceAccount financeAccount : financeAccountsList) {
            financeAccount.setSum(new Double(0));
            financeAccountHashMap.put(financeAccount.getAccountNumber(), financeAccount);
        }

        //Calculate sum of individual accounts
        for (BookedFinancePosting bookedFinancePosting : financePostingList) {
            int id = bookedFinancePosting.getFinanceAccount().getAccountNumber();
            FinanceAccount financeAccount = financeAccountHashMap.get(id);
            financeAccount.setSum(financeAccount.getSum() + bookedFinancePosting.getAmount());
        }
        return financeAccountsList;
    }

    /**
     * Used for balance report
     *
     * @param legalEntity current legal entity
     * @param model       report model object
     * @return financeaccounts with sums
     */
    @Transactional(readOnly = true)
    public List<FinanceAccount> getFinanceAccountsWithSum(LegalEntity legalEntity, CompoundPropertyModel<ReportObject> model) {

        log.debug("Getting financeaccountswith sum");
        Date fromDate = model.getObject().getDateFrom();
        Date toDate = model.getObject().getDateTo();

        Date fromDateCompare = model.getObject().getDateFromCompare();
        Date toDateCompare = model.getObject().getDateToCompare();

        FinanceAccount fromAccount = model.getObject().getAccountFrom();
        FinanceAccount toAccount = model.getObject().getAccountTo();

        Double year_result = new Double(0);
        Double year_result_compare = new Double(0);


        if (fromDate == null || toDate == null || fromAccount == null || toAccount == null) {
            throw new NullPointerException("Arguments must not be null");
        }

        List<FinanceAccount> financeAccountsList = financeAccountService.findFinanceAccountByLegalEntityFromAccountToAccount(legalEntity, fromAccount, toAccount);
        List<BookedFinancePosting> financePostingList = postingService.findBookedPostingsFromLegalEntityFromDateToDate(legalEntity, fromDate, toDate);
        List<BookedFinancePosting> financePostingListCompare = postingService.findBookedPostingsFromLegalEntityFromDateToDate(legalEntity, fromDateCompare, toDateCompare);

//        Collections.sort(financePostingList, new BookedFinancePostingDateComparator());

        HashMap<Integer, FinanceAccount> financeAccountHashMap = new HashMap<Integer, FinanceAccount>();
        //Arrange and set sum = 0
        for (FinanceAccount financeAccount : financeAccountsList) {
            financeAccount.setSum(new Double(0));
            financeAccount.setSumCompare(new Double(0));
            financeAccountHashMap.put(financeAccount.getAccountNumber(), financeAccount);
        }

        //Calculate sum of individual accounts and sum of year result
        for (BookedFinancePosting bookedFinancePosting : financePostingList) {
            int id = bookedFinancePosting.getFinanceAccount().getAccountNumber();
            FinanceAccount financeAccount = financeAccountHashMap.get(id);
            financeAccount.setSum(financeAccount.getSum() + bookedFinancePosting.getAmount());

            if (financeAccount.getFinanceAccountType().isOperating_account())
                year_result += bookedFinancePosting.getAmount();
        }

        //Calculate sum of compare accounts and sum of year result
        for (BookedFinancePosting bookedFinancePostingCompare : financePostingListCompare) {
            int id = bookedFinancePostingCompare.getFinanceAccount().getAccountNumber();
            FinanceAccount financeAccount = financeAccountHashMap.get(id);
            financeAccount.setSumCompare(financeAccount.getSumCompare() + bookedFinancePostingCompare.getAmount());

            if (financeAccount.getFinanceAccountType().isOperating_account())
                year_result_compare += bookedFinancePostingCompare.getAmount();
        }


        //Set sum for current result
        for (FinanceAccount financeAccount : financeAccountsList) {
            if (financeAccount.getFinanceAccountType().equals(FinanceAccountType.CURRENT_RESULT)) {
                financeAccount.setSum(year_result);
                financeAccount.setSumCompare(year_result_compare);
            }
        }

        //Calculate sum of sum accounts
        for (FinanceAccount financeAccount : financeAccountsList) {
            if (financeAccount.getFinanceAccountType() == FinanceAccountType.SUM || financeAccount.getFinanceAccountType() == FinanceAccountType.CATEGORY_SUM) {

                int sumFrom = financeAccount.getSumFrom().getAccountNumber();
                int sumTo = financeAccount.getSumTo().getAccountNumber();

                for (FinanceAccount entry : financeAccountHashMap.values()) {
                    if (entry.getAccountNumber() >= sumFrom && entry.getAccountNumber() <= sumTo && entry.getFinanceAccountType() != FinanceAccountType.SUM) {
                        financeAccount.setSum(financeAccount.getSum() + entry.getSum());
                        financeAccount.setSumCompare(financeAccount.getSumCompare() + entry.getSumCompare());
                    }
                }
            } else if (financeAccount.getFinanceAccountType().equals(FinanceAccountType.BALANCE_CHECK)) {

                for (FinanceAccount entry : financeAccountHashMap.values()) {
                    if (entry.getFinanceAccountType().isOperating_account() || entry.getFinanceAccountType().isBalance_account()) {
                        financeAccount.setSum(financeAccount.getSum() + entry.getSum());
                        financeAccount.setSumCompare(financeAccount.getSumCompare() + entry.getSumCompare());
                    }
                }
            }
        }

        //TODO create better way to hide accounts without sum, taking account for headers, categories and sum accounts
        if (model.getObject().isHideAccountsWithOutSum()) {
            List<FinanceAccount> onlyWithSum = new ArrayList<FinanceAccount>();
            for (FinanceAccount financeAccount : financeAccountsList) {
                if ((!financeAccount.getSum().equals(new Double(0)) || !financeAccount.getSumCompare().equals(new Double(0))) || financeAccount.getFinanceAccountType().includeInOnlyWithSum())
                    onlyWithSum.add(financeAccount);
            }
            financeAccountsList = onlyWithSum;
        }

        if (financeAccountsList.size() == 0) {
            model.getObject().setEmptyReport(true);
        } else {
            model.getObject().setEmptyReport(false);
        }


        List<FinanceAccount> withExtraEmptyRows = new ArrayList<>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            withExtraEmptyRows.add(financeAccount);
            if (financeAccount.getFinanceAccountType().addExtraRowToBalanceReport()) {
                withExtraEmptyRows.add(new FinanceAccount().setFinanceAccountType(FinanceAccountType.EMPTY_ROW));
            }
        }
        financeAccountsList = withExtraEmptyRows;

        return financeAccountsList;
    }

    @Transactional(readOnly = true)
    public List<BookedFinancePosting> getBookedFinancePostingsWithSum(LegalEntity id, CompoundPropertyModel<ReportObject> cpm) {

        log.debug("Getting BookedFinancepostings with sum");
        Date fromDate = cpm.getObject().getDateFrom();
        Date toDate = cpm.getObject().getDateTo();

        FinanceAccount fromAccount = cpm.getObject().getAccountFrom();
        FinanceAccount toAccount = cpm.getObject().getAccountTo();

        List<FinanceAccount> financeAccountsList = financeAccountService.findBookableFinanceAccountByLegalEntityFromAccountToAccount(id, fromAccount, toAccount);
        List<BookedFinancePosting> financePostingList = postingService.findBookedPostingsFromLegalEntityByIntervalFromDateToDate(financeAccountsList, fromDate, toDate);

        Collections.sort(financePostingList, new BookedFinancePostingDateComparator());

        HashMap<Integer, FinanceAccount> financeAccountHashMap = new HashMap<Integer, FinanceAccount>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            financeAccountHashMap.put(financeAccount.getAccountNumber(), financeAccount);
        }

        for (BookedFinancePosting bookedFinancePosting : financePostingList) {
            financeAccountHashMap.get(bookedFinancePosting.getFinanceAccount().getAccountNumber()).getBookedFinancePostingList().add(bookedFinancePosting);
        }

        for (FinanceAccount financeAccount : financeAccountsList) {
            BookedFinancePosting previous = null;
            for (BookedFinancePosting bookedFinancePosting : financeAccount.getBookedFinancePostingList()) {
                if (previous == null) {
                    bookedFinancePosting.setSum(bookedFinancePosting.getAmount());
                } else {
                    bookedFinancePosting.setSum(previous.getSum() + bookedFinancePosting.getAmount());
                }
                previous = bookedFinancePosting;
            }
        }

        if (cpm.getObject().isHideAccountsWithOutSum()) {
            List<FinanceAccount> onlyWithSum = new ArrayList<FinanceAccount>();
            for (FinanceAccount financeAccount : financeAccountsList) {

                if (financeAccount.getBookedFinancePostingList().size() > 0 &&
                        !ZERO.equals(financeAccount.getBookedFinancePostingList().get(financeAccount.getBookedFinancePostingList().size() - 1).getSum())) {
                    onlyWithSum.add(financeAccount);
                }
            }
            financeAccountsList = onlyWithSum;
        }

        if (financeAccountsList.size() == 0) {
            cpm.getObject().setEmptyReport(true);
        } else {
            cpm.getObject().setEmptyReport(false);
        }

        Collections.sort(financeAccountsList, new FinanceAccountNumberComparator());

        List<BookedFinancePosting> bookedFinancePostings = new ArrayList<>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            bookedFinancePostings.addAll(financeAccount.getBookedFinancePostingList());
        }
        return bookedFinancePostings;
    }

    @Transactional(readOnly = true)
    public List<FinanceAccount> getFinanceAccountsWithBookedFinancePostings(LegalEntity id, CompoundPropertyModel<ReportObject> cpm) {

        log.debug("Getting FinanceAccounts With BookedFinance Postings");
        Date fromDate = cpm.getObject().getDateFrom();
        Date toDate = cpm.getObject().getDateTo();

        FinanceAccount fromAccount = cpm.getObject().getAccountFrom();
        FinanceAccount toAccount = cpm.getObject().getAccountTo();

        List<FinanceAccount> financeAccountsList = financeAccountService.findFinanceAccountByLegalEntityFromAccountToAccount(id, fromAccount, toAccount);
        List<BookedFinancePosting> financePostingList = postingService.findBookedPostingsFromLegalEntityByIntervalFromDateToDate(financeAccountsList, fromDate, toDate);

        Collections.sort(financePostingList, new BookedFinancePostingDateComparator());

        List<FinanceAccount> onlyOperationsAndBalanceAccounts = new ArrayList<FinanceAccount>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            if (financeAccount.isBookable()) onlyOperationsAndBalanceAccounts.add(financeAccount);
        }
        financeAccountsList = onlyOperationsAndBalanceAccounts;

        HashMap<Integer, FinanceAccount> financeAccountHashMap = new HashMap<Integer, FinanceAccount>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            financeAccountHashMap.put(financeAccount.getAccountNumber(), financeAccount);
        }

        for (BookedFinancePosting bookedFinancePosting : financePostingList) {
            financeAccountHashMap.get(bookedFinancePosting.getFinanceAccount().getAccountNumber()).getBookedFinancePostingList().add(bookedFinancePosting);
        }

        if (cpm.getObject().isHideAccountsWithOutTransactions()) {
            List<FinanceAccount> onlyWithTransactions = new ArrayList<FinanceAccount>();
            for (FinanceAccount financeAccount : financeAccountsList) {
                if (financeAccount.getBookedFinancePostingList().size() > 0) {
                    onlyWithTransactions.add(financeAccount);
                }
            }
            financeAccountsList = onlyWithTransactions;
        }

        for (FinanceAccount financeAccount : financeAccountsList) {
            BookedFinancePosting previous = null;
            for (BookedFinancePosting bookedFinancePosting : financeAccount.getBookedFinancePostingList()) {
                if (previous == null) {
                    bookedFinancePosting.setSum(bookedFinancePosting.getAmount());
                } else {
                    bookedFinancePosting.setSum(previous.getSum() + bookedFinancePosting.getAmount());
                }
                previous = bookedFinancePosting;
            }
        }

        if (cpm.getObject().isHideAccountsWithOutSum()) {
            List<FinanceAccount> onlyWithSum = new ArrayList<FinanceAccount>();
            for (FinanceAccount financeAccount : financeAccountsList) {

                if (financeAccount.getBookedFinancePostingList().size() > 0 &&
                        !ZERO.equals(financeAccount.getBookedFinancePostingList().get(financeAccount.getBookedFinancePostingList().size() - 1).getSum())) {
                    onlyWithSum.add(financeAccount);
                }
            }
            financeAccountsList = onlyWithSum;
        }

        if (financeAccountsList.size() == 0) {
            cpm.getObject().setEmptyReport(true);
        } else {
            cpm.getObject().setEmptyReport(false);
        }

        return financeAccountsList;

    }


}

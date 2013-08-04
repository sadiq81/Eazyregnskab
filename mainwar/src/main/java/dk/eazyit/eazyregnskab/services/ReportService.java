package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.util.BookedFinancePostingDateComparator;
import dk.eazyit.eazyregnskab.util.ReportObject;
import org.apache.wicket.model.CompoundPropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author
 */
@Service
public class ReportService {

    @Autowired
    FinanceAccountService financeAccountService;
    @Autowired
    PostingService postingService;

    @Transactional
    public List<FinanceAccount> getFinanceAccountsWithSum(FiscalYear fiscalYear) {

        List<FinanceAccount> financeAccountsList = financeAccountService.findFinanceAccountByLegalEntity(fiscalYear.getLegalEntity());

        List<BookedFinancePosting> financePostingList = new ArrayList<BookedFinancePosting>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            List<BookedFinancePosting> temp = postingService.findBookedPostingsFromFinanceAccountFromDateToDate(financeAccount, fiscalYear.getStart(), fiscalYear.getEnd());
            financePostingList.addAll(temp);
        }

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

    @Transactional
    public List<FinanceAccount> getFinanceAccountsWithSum(LegalEntity legalEntity, CompoundPropertyModel<ReportObject> model) {

        Date fromDate = model.getObject().getDateFrom();
        Date toDate = model.getObject().getDateTo();

        Date fromDateCompare = model.getObject().getDateFromCompare();
        Date toDateCompare = model.getObject().getDateToCompare();

        FinanceAccount fromAccount = model.getObject().getAccountFrom();
        FinanceAccount toAccount = model.getObject().getAccountTo();

        FinanceAccount current_result;
        Double year_result = new Double(0);
        Double year_result_compare = new Double(0);


        if (fromDate == null || toDate == null || fromAccount == null || toAccount == null) {
            throw new NullPointerException("Arguments must not be null");
        }

        List<FinanceAccount> financeAccountsList = financeAccountService.findFinanceAccountByLegalEntityFromAccountToAccount(legalEntity, fromAccount, toAccount);
        List<BookedFinancePosting> financePostingList = new ArrayList<BookedFinancePosting>();
        for (FinanceAccount financeAccount : financeAccountsList) {

            if (financeAccount.getFinanceAccountType().equals(FinanceAccountType.CURRENT_RESULT))
                current_result = financeAccount;

            List<BookedFinancePosting> temp = postingService.findBookedPostingsFromFinanceAccountFromDateToDate(financeAccount, fromDate, toDate);
            financePostingList.addAll(temp);
        }

        List<BookedFinancePosting> financePostingListCompare = new ArrayList<BookedFinancePosting>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            List<BookedFinancePosting> temp = postingService.findBookedPostingsFromFinanceAccountFromDateToDate(financeAccount, fromDateCompare, toDateCompare);
            financePostingListCompare.addAll(temp);
        }

        Collections.sort(financePostingList, new BookedFinancePostingDateComparator());

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
        for (BookedFinancePosting bookedFinancePosting : financePostingListCompare) {
            int id = bookedFinancePosting.getFinanceAccount().getAccountNumber();
            FinanceAccount financeAccount = financeAccountHashMap.get(id);
            financeAccount.setSumCompare(financeAccount.getSumCompare() + bookedFinancePosting.getAmount());

            if (financeAccount.getFinanceAccountType().isOperating_account())
                year_result_compare += bookedFinancePosting.getAmount();
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
            if (financeAccount.getFinanceAccountType() == FinanceAccountType.SUM) {

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

        return financeAccountsList;
    }
}

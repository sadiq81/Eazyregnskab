package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
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

    @Transactional
    public List<FinanceAccount> getFinanceAccountsWithSum(LegalEntity legalEntity, CompoundPropertyModel<ReportObject> model) {

        Date fromDate = model.getObject().getDateFrom();
        Date toDate = model.getObject().getDateTo();

        FinanceAccount fromAccount = model.getObject().getAccountFrom();
        FinanceAccount toAccount = model.getObject().getAccountTo();

        if (fromDate == null || toDate == null || fromAccount == null || toAccount == null) {
            throw new NullPointerException("Arguments must not be null");
        }

        //TODO Calculate sums in DB??
        List<FinanceAccount> financeAccountsList = financeAccountService.findFinanceAccountByLegalEntityFromAccountToAccount(legalEntity, fromAccount, toAccount);
        List<BookedFinancePosting> financePostingList = new ArrayList<BookedFinancePosting>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            List <BookedFinancePosting> temp = financeAccountService.findPostingsFromFinanceAccountFromDateToDate(financeAccount, fromDate, toDate);
            financePostingList.addAll(temp);
        }

        Collections.sort(financePostingList, new BookedFinancePostingDateComparator());

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

        //Calculate sum of sum accounts
        for (FinanceAccount financeAccount : financeAccountsList) {
            if (financeAccount.getFinanceAccountType() == FinanceAccountType.SUM) {

                int sumFrom = financeAccount.getSumFrom().getAccountNumber();
                int sumTo = financeAccount.getSumTo().getAccountNumber();

                for (FinanceAccount entry : financeAccountHashMap.values()) {
                    if (entry.getAccountNumber() >= sumFrom && entry.getAccountNumber() <= sumTo) {
                        financeAccount.setSum(financeAccount.getSum() + entry.getSum());
                    }
                }
            }
        }

        return financeAccountsList;
    }
}

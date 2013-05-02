package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.util.BookedFinancePostingDateComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author
 */
@Service
public class ReportService {

    @Autowired
    FinanceAccountService financeAccountService;

    @Transactional
    public List<FinanceAccount> getFinanceAccountsWithSum(LegalEntity legalEntity) {

        //TODO Calculate sums in DB
        List<FinanceAccount> financeAccountsList = financeAccountService.findFinanceAccountByLegalEntity(legalEntity);
        List<BookedFinancePosting> financePostingList = financeAccountService.findPostingsFromLegalEntity(legalEntity);
        Collections.sort(financePostingList, new BookedFinancePostingDateComparator());

        HashMap<Long, FinanceAccount> financeAccountHashMap = new HashMap<Long, FinanceAccount>();
        for (FinanceAccount financeAccount : financeAccountsList) {
            financeAccount.setSum(new Double(0));
            financeAccountHashMap.put(financeAccount.getId(), financeAccount);
        }

        for (BookedFinancePosting bookedFinancePosting : financePostingList){
            Long id = bookedFinancePosting.getFinanceAccount().getId();
            FinanceAccount financeAccount = financeAccountHashMap.get(id);
            financeAccount.setSum(financeAccount.getSum() + bookedFinancePosting.getAmount());
        }

        return financeAccountsList;
    }


}

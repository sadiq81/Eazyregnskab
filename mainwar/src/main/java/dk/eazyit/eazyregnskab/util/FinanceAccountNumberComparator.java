package dk.eazyit.eazyregnskab.util;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;

import java.util.Comparator;

/**
 * @author
 */
public class FinanceAccountNumberComparator implements Comparator<FinanceAccount> {
    @Override
    public int compare(FinanceAccount financeAccount, FinanceAccount financeAccount2) {
        return financeAccount.getAccountNumber().compareTo(financeAccount2.getAccountNumber());
    }
}

package dk.eazyit.eazyregnskab.util.comparetors;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author
 */
public class FinanceAccountNumberComparator implements Comparator<FinanceAccount>, Serializable {
    @Override
    public int compare(FinanceAccount financeAccount, FinanceAccount financeAccount2) {
        return financeAccount.getAccountNumber().compareTo(financeAccount2.getAccountNumber());
    }
}

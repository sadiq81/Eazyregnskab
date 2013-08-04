package dk.eazyit.eazyregnskab.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public enum FinanceAccountType {

    PROFIT(false, true),
    EXPENSE(false, true),
    ASSET(false, false),
    LIABILITY(false, false),
    YEAR_END(true, false),
    CURRENT_RESULT(true, false),
    CATEGORY(false, false),
    HEADLINE(false, false),
    SUM(false, false),
    BALANCE_CHECK(true, false);

    private FinanceAccountType(boolean system_account, boolean profit_or_expense) {
        this.system_account = system_account;
        this.profit_or_expense = profit_or_expense;
    }

    public static List<FinanceAccountType> getNonSystemAccounts() {
        ArrayList<FinanceAccountType> nonSystem = new ArrayList<FinanceAccountType>();
        for (FinanceAccountType type : FinanceAccountType.values()) {
            if (!type.isSystem_account()) nonSystem.add(type);
        }
        return nonSystem;
    }

    public static List<FinanceAccountType> getSystemAccounts() {
        ArrayList<FinanceAccountType> system = new ArrayList<FinanceAccountType>();
        for (FinanceAccountType type : FinanceAccountType.values()) {
            if (type.isSystem_account()) system.add(type);
        }
        return system;
    }

    public boolean isSystem_account() {
        return system_account;
    }

    public boolean isProfit_or_expense() {
        return profit_or_expense;
    }

    private boolean system_account;
    private boolean profit_or_expense;
}

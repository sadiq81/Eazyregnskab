package dk.eazyit.eazyregnskab.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public enum FinanceAccountType {

    PROFIT(false, true, false, false),
    EXPENSE(false, true, false, false),
    ASSET(false, false, true, false),
    LIABILITY(false, false, true, false),
    YEAR_END(true, false, true, false),
    CURRENT_RESULT(true, false, false, false),
    CATEGORY(false, false, false, true),
    HEADLINE(false, false, false, true),
    SUM(false, false, false, true),
    BALANCE_CHECK(true, false, false, true);

    private FinanceAccountType(boolean system_account, boolean operating_account, boolean balance_account, boolean includeInOnlyWithSum) {
        this.system_account = system_account;
        this.operating_account = operating_account;
        this.balance_account = balance_account;
        this.includeInOnlyWithSum = includeInOnlyWithSum;
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

    public boolean isOperating_account() {
        return operating_account;
    }

    public boolean isBalance_account() {
        return balance_account;
    }

    private boolean system_account;
    private boolean operating_account;
    private boolean balance_account;
    private boolean includeInOnlyWithSum;

    public boolean includeInOnlyWithSum() {
        return includeInOnlyWithSum;
    }
}

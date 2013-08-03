package dk.eazyit.eazyregnskab.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public enum FinanceAccountType {

    PROFIT(false),
    EXPENSE(false),
    ASSET(false),
    LIABILITY(false),
    YEAR_END(true),
    CATEGORY(false),
    HEADLINE(false),
    SUM(false);

    private FinanceAccountType(boolean system_account) {
        this.system_account = system_account;
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

    private boolean system_account;
}

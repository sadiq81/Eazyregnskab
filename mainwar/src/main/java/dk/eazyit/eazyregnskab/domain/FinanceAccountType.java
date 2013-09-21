package dk.eazyit.eazyregnskab.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public enum FinanceAccountType {

    PROFIT(false, true, false, true),
    EXPENSE(false, true, false, true),
    ASSET(false, false, true, true),
    LIABILITY(false, false, true, true),
    YEAR_END(true, false, true, true),
    CURRENT_RESULT(true, false, false, true),
    CATEGORY(false, false, false, false),
    HEADLINE(false, false, false, false),
    SUM(false, false, false, true),
    CATEGORY_SUM(false, false, false, true),
    BALANCE_CHECK(true, false, false, true),
    EMPTY_ROW(true, false, false, false);

    private FinanceAccountType(boolean system_account, boolean operating_account, boolean balance_account, boolean has_sum) {
        this.system_account = system_account;
        this.operating_account = operating_account;
        this.balance_account = balance_account;
        this.has_sum = has_sum;
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

    public boolean isHas_sum() {
        return has_sum;
    }

    private boolean system_account;
    private boolean operating_account;
    private boolean balance_account;
    private boolean has_sum;

    public boolean includeInOnlyWithSum() {
        return this == CATEGORY || this == HEADLINE || this == BALANCE_CHECK || this == SUM || this == CATEGORY_SUM;
    }

    public boolean addExtraRowToBalanceReport() {
        return this == CATEGORY || this == SUM || this == CATEGORY_SUM;
    }
}

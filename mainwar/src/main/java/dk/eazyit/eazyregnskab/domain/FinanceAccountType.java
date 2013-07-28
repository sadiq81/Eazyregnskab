package dk.eazyit.eazyregnskab.domain;

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

    public boolean isSystem_account() {
        return system_account;
    }

    public void setSystem_account(boolean system_account) {
        this.system_account = system_account;
    }

    private boolean system_account;
}

package dk.eazyit.eazyregnskab.services.importer.financeaccount;

import dk.eazyit.eazyregnskab.domain.FinanceAccountType;

import java.io.Serializable;

/**
 * @author
 */
public class FinanceAccountLine implements Serializable {

    String accountNumber;
    String accountName;
    String vatType;
    String accountFrom;
    String accountTo;
    FinanceAccountType type;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getVatType() {
        return vatType;
    }

    public void setVatType(String vatType) {
        this.vatType = vatType;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

    public FinanceAccountType getType() {
        return type;
    }

    public void setType(FinanceAccountType type) {
        this.type = type;
    }
}


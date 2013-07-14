package dk.eazyit.eazyregnskab.util;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
public class ReportObject implements Serializable{

    protected Date dateFrom;
    protected Date dateTo;
    protected FinanceAccount accountFrom;
    protected FinanceAccount accountTo;

    public ReportObject() {
    }

    public ReportObject(FinanceAccount accountFrom, FinanceAccount accountTo) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    public ReportObject(Date dateFrom, Date dateTo, FinanceAccount accountFrom, FinanceAccount accountTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public FinanceAccount getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(FinanceAccount accountFrom) {
        this.accountFrom = accountFrom;
    }

    public FinanceAccount getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(FinanceAccount accountTo) {
        this.accountTo = accountTo;
    }
}

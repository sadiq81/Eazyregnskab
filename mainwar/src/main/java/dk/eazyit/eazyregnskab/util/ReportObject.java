package dk.eazyit.eazyregnskab.util;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.ReportCompareType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
public class ReportObject implements Serializable {

    public static final String ATTRIBUTE_NAME = ReportObject.class.getName();

    //TODO all date calculations expect accounting year follows normal year
    protected Date dateFrom;
    protected Date dateTo;
    protected Date dateFromCompare;
    protected Date dateToCompare;
    protected FinanceAccount accountFrom;
    protected FinanceAccount accountTo;
    protected ReportCompareType reportCompareType = ReportCompareType.COMPARE_WITH_SAME_PERIOD_LAST_YEAR;
    protected boolean hideAccountsWithOutSum = true;
    protected boolean hideAccountsWithOutTransactions = true;
    protected boolean submitHasBeenPressed;
    protected boolean emptyReport;

    public ReportObject() {
        dateFrom = CalendarUtil.getFirstDayInYear();
        dateTo = CalendarUtil.getLastDayInYear();
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

    public String getDates() {
        return CalendarUtil.getSimpleDateString(getDateFrom()) + " - " + CalendarUtil.getSimpleDateString(getDateTo());
    }

    public String getDatesCompare() {
        return CalendarUtil.getSimpleDateString(getDateFromCompare()) + " - " + CalendarUtil.getSimpleDateString(getDateToCompare());
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

    public Date getDateFromCompare() {

        switch (getReportCompareType()) {
            case COMPARE_YEAR_TO_DATE: {
                return CalendarUtil.getFirstDayInYear(getDateFrom());
            }
            case COMPARE_WITH_SAME_PERIOD_LAST_YEAR: {
                return CalendarUtil.subtractOneYear(getDateFrom());
            }
            case COMPARE_TO_BUDGET: {
                break;
            }
        }
        return getDateFrom();
    }

    public void setDateFromCompare(Date dateFromCompare) {
        this.dateFromCompare = dateFromCompare;
    }

    public Date getDateToCompare() {
        switch (getReportCompareType()) {
            case COMPARE_YEAR_TO_DATE: {
                return getDateTo();
            }
            case COMPARE_WITH_SAME_PERIOD_LAST_YEAR: {
                return CalendarUtil.subtractOneYear(getDateTo());
            }
            case COMPARE_TO_BUDGET: {
                break;
            }
            default: {
                return getDateTo();
            }
        }
        return getDateTo();
    }

    public void setDateToCompare(Date dateToCompare) {
        this.dateToCompare = dateToCompare;
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

    public ReportCompareType getReportCompareType() {
        return reportCompareType;
    }

    public void setReportCompareType(ReportCompareType reportCompareType) {
        this.reportCompareType = reportCompareType;
    }

    public boolean isHideAccountsWithOutSum() {
        return hideAccountsWithOutSum;
    }

    public void setHideAccountsWithOutSum(boolean hideAccountsWithOutSum) {
        this.hideAccountsWithOutSum = hideAccountsWithOutSum;
    }

    public boolean isHideAccountsWithOutTransactions() {
        return hideAccountsWithOutTransactions;
    }

    public void setHideAccountsWithOutTransactions(boolean hideAccountsWithOutTransactions) {
        this.hideAccountsWithOutTransactions = hideAccountsWithOutTransactions;
    }

    public boolean isSubmitHasBeenPressed() {
        return submitHasBeenPressed;
    }

    public void setSubmitHasBeenPressed(boolean submitHasBeenPressed) {
        this.submitHasBeenPressed = submitHasBeenPressed;
    }

    public boolean isEmptyReport() {
        return emptyReport;
    }

    public void setEmptyReport(boolean emptyReport) {
        this.emptyReport = emptyReport;
    }
}

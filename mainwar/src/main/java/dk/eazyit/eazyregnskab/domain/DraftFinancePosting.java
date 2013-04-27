package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
@Entity
@NamedQueries({
        @NamedQuery(name = DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, query = "select fp from DraftFinancePosting fp " +
                "WHERE fp.financeAccount = ?1 or fp.reverseFinanceAccount = ?1"),
        @NamedQuery(name = DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, query = "select fp from DraftFinancePosting fp " +
                "WHERE fp.dailyLedger = ?1"),
        @NamedQuery(name = DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, query = "select fp from DraftFinancePosting fp " +
                "WHERE fp.vatType = ?1")
})
@Table(name = "draftfinanceposting")
public class DraftFinancePosting extends BaseEntity {

    public static final String QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT = "DraftFinancePosting::findDraftFinancePostingByFinanceAccount";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER = "DraftFinancePosting::findDraftFinancePostingByDailyLedger";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE = "DraftFinancePosting::findDraftFinancePostingByVatType";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 50)
    private int bookingNumber;

    @Column(unique = false, nullable = false, length = 50)
    private Date date;

    @Column(unique = false, nullable = false, length = 50)
    private String text;

    @Column(unique = false, nullable = false, precision = 15, scale = 2)
    private Double amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "financeaccount_id")
    private FinanceAccount financeAccount;

    @ManyToOne(optional = true)
    @JoinColumn(name = "reversefinanceaccount_id")
    private FinanceAccount reverseFinanceAccount;

    @ManyToOne(optional = true)
    @JoinColumn(name = "dailyledger_id")
    private DailyLedger dailyLedger;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "vatType_id")
    private VatType vatType;

    @Transient
    private double vatAmount;

    @Column
    private boolean chosen;

    public DraftFinancePosting() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(int bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public FinanceAccount getFinanceAccount() {
        return financeAccount;
    }

    public void setFinanceAccount(FinanceAccount financeAccount) {
        this.financeAccount = financeAccount;
    }

    public FinanceAccount getReverseFinanceAccount() {
        return reverseFinanceAccount;
    }

    public void setReverseFinanceAccount(FinanceAccount reverseFinanceAccount) {
        this.reverseFinanceAccount = reverseFinanceAccount;
    }

    public DailyLedger getDailyLedger() {
        return dailyLedger;
    }

    public DraftFinancePosting setDailyLedger(DailyLedger dailyLedger) {
        this.dailyLedger = dailyLedger;
        return this;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public VatType getVatType() {
        return vatType;
    }

    public void setVatType(VatType vatType) {
        this.vatType = vatType;
    }

    public double getVatAmount() {
        if (vatType != null && vatType.getPercentage() != null && amount != null) {
            return vatType.getPercentage() * amount / 100;
        } else {
            return 0;
        }
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("bookingNumber", bookingNumber)
                .add("date", date)
                .add("text", text)
                .add("amount", amount)
//                .add("financeAccount", financeAccount)
//                .add("reverseFinanceAccount", reverseFinanceAccount)
//                .add("dailyLedger", dailyLedger)
//                .add("vatType", vatType)
                .add("chosen", chosen)
                .toString();
    }
}

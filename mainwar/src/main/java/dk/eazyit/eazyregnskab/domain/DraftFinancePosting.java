package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
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
                "WHERE fp.vatType = ?1 or fp.reverseVatType =?1 "),

        @NamedQuery(name = DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_IN_YEAR, query = "select fp from DraftFinancePosting fp " +
                "WHERE (fp.financeAccount.legalEntity = ?1 or fp.reverseFinanceAccount.legalEntity =?1) and fp.date >= ?2 and fp.date <= ?3 ")
})
@Table(name = "draftfinanceposting")
public class DraftFinancePosting extends BaseEntity {

    public static final String QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT = "DraftFinancePosting::findDraftFinancePostingByFinanceAccount";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER = "DraftFinancePosting::findDraftFinancePostingByDailyLedger";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE = "DraftFinancePosting::findDraftFinancePostingByVatType";
    public static final String QUERY_FIND_FINANCE_POSTING_IN_YEAR = "DraftFinancePosting::findDraftFinancePostingInYear";

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

    @ManyToOne(optional = true)
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

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "reverseVatType_id")
    private VatType reverseVatType;

    @Transient
    private double vatAmount;

    @Transient
    private boolean chosen;

    public DraftFinancePosting() {
    }

    public DraftFinancePosting(BookedFinancePosting bookedFinancePosting) {
        this.bookingNumber = bookedFinancePosting.getBookingNumber();
        this.date = bookedFinancePosting.getDate();
        this.text = bookedFinancePosting.getText();
        this.amount = bookedFinancePosting.getAmount();
        this.financeAccount = bookedFinancePosting.getFinanceAccount();
    }

    public DraftFinancePosting(int bookingNumber) {
        this.bookingNumber = bookingNumber;
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

    public DraftFinancePosting setBookingNumber(int bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public DraftFinancePosting setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getText() {
        return text;
    }

    public DraftFinancePosting setText(String text) {
        this.text = text;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void addAmount(Double amount) {
        this.amount += amount;
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

    public DraftFinancePosting setChosen(boolean chosen) {
        this.chosen = chosen;
        return this;
    }

    public VatType getVatType() {
        return vatType;
    }

    public void setVatType(VatType vatType) {
        this.vatType = vatType;
    }

    public VatType getReverseVatType() {
        return reverseVatType;
    }

    public void setReverseVatType(VatType reverseVatType) {
        this.reverseVatType = reverseVatType;
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


    public DraftFinancePosting getPostingForReverse() {
        DraftFinancePosting reverse = new DraftFinancePosting();
        reverse.setBookingNumber(this.getBookingNumber());
        reverse.setDate(this.getDate());
        reverse.setText(this.getText());
        reverse.setAmount(0 - this.getAmount());
        reverse.setFinanceAccount(this.getReverseFinanceAccount());
        reverse.setReverseFinanceAccount(null);
        reverse.setVatType(this.getReverseVatType());
        reverse.setReverseVatType(null);
        return reverse;
    }

    public DraftFinancePosting reverseAmount() {
        amount = -amount;
        return this;
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

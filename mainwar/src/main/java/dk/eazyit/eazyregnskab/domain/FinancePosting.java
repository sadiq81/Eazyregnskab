package dk.eazyit.eazyregnskab.domain;

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
        @NamedQuery(name = FinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, query = "select fp from FinancePosting fp " +
                "WHERE fp.financeAccount = ?1 or fp.reverseFinanceAccount = ?1"),
        @NamedQuery(name = FinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, query = "select fp from FinancePosting fp " +
                "WHERE fp.dailyLedger = ?1")
})
@Table(name = "financeposting")
public class FinancePosting extends BaseEntity {

    public static final String QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT = "FinancePosting::findFinancePostingByFinanceAccount";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER = "FinancePosting::findFinancePostingByDailyLedger";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 50)
    private Date date;

    @Column(unique = false, nullable = false, length = 50)
    private String text;

    @Column(unique = false, nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(unique = false, nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private FinancePostingStatus financePostingStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "financeaccount_id")
    private FinanceAccount financeAccount;

    @ManyToOne(optional = true)
    @JoinColumn(name = "reversefinanceaccount_id")
    private FinanceAccount reverseFinanceAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dailyledger_id")
    private DailyLedger dailyLedger;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "vatType_id")
    private VatType vatType;

    public FinancePosting() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public FinancePostingStatus getFinancePostingStatus() {
        return financePostingStatus;
    }

    public void setFinancePostingStatus(FinancePostingStatus financePostingStatus) {
        this.financePostingStatus = financePostingStatus;
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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public DailyLedger getDailyLedger() {
        return dailyLedger;
    }

    public FinancePosting setDailyLedger(DailyLedger dailyLedger) {
        this.dailyLedger = dailyLedger;
        return this;
    }

    public VatType getVatType() {
        return vatType;
    }

    public void setVatType(VatType vatType) {
        this.vatType = vatType;
    }

    @Override
    public String toString() {
        return "FinancePosting{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", amount=" + amount +
                ", financePostingStatus=" + financePostingStatus +
                ", financeAccount=" + financeAccount.getName() +
                ", appUser=" + appUser.getUsername() +
                ", dailyLedger=" + dailyLedger.getName() +
                '}';
    }
}

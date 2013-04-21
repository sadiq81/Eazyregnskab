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
        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, query = "select fp from DraftFinancePosting fp " +
                "WHERE fp.financeAccount = ?1"),
        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, query = "select fp from DraftFinancePosting fp " +
                "WHERE fp.vatType = ?1")

})
@Table(name = "bookedfinanceposting")
public class BookedFinancePosting extends BaseEntity {

    public static final String QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT = "BookedFinancePosting::findBookedFinancePostingByFinanceAccount";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE = "BookedFinancePosting::findBookedFinancePostingByVatType";

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
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "vatType_id")
    private VatType vatType;

    public BookedFinancePosting() {
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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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
                ", financeAccount=" + financeAccount.getName() +
                '}';
    }
}

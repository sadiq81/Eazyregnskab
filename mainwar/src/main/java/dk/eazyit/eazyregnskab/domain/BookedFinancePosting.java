package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 */
@Entity
@NamedQueries({
        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT, query = "select bp from BookedFinancePosting bp " +
                "WHERE bp.financeAccount = ?1"),

        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_ACCOUNT_FROM_DATE_TO_DATE, query = "select bp from BookedFinancePosting bp " +
                "WHERE bp.financeAccount = ?1 and bp.date >= ?2 and bp.date <= ?3"),

        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE, query = "select bp from BookedFinancePosting bp " +
                "WHERE bp.vatType = ?1"),

        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_LEGAL_ENTITY, query = "select bp from BookedFinancePosting bp " +
                "WHERE bp.financeAccount.id in (select fp.id from FinanceAccount fp where fp.legalEntity = ?1)"),

})
@Table(name = "bookedfinanceposting")
public class BookedFinancePosting extends BaseEntity {

    public static final String QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT = "BookedFinancePosting::findBookedFinancePostingByFinanceAccount";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_ACCOUNT_FROM_DATE_TO_DATE = "BookedFinancePosting::findBookedFinancePostingByFinanceAccountsFromDateToDate";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_LEGAL_ENTITY = "BookedFinancePosting::findBookedFinancePostingByLegalEntity";
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookedFinancePostingType bookedFinancePostingType;

    @Transient
    private Double sum;

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

    public String getSimpleFormatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-YYYY");
        return sdf.format(getDate());
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

    public void removeVat(Double vat) {
        amount -= vat;
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

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public BookedFinancePostingType getBookedFinancePostingType() {
        return bookedFinancePostingType;
    }

    public void setBookedFinancePostingType(BookedFinancePostingType bookedFinancePostingType) {
        this.bookedFinancePostingType = bookedFinancePostingType;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("bookingNumber", bookingNumber)
                .add("date", date)
                .add("text", text)
                .add("amount", amount)
                .add("financeAccount", financeAccount.getName())
                .add("appUser", appUser != null ? appUser.getUsername() : null)
//                .add("vatType", vatType)
                .toString();
    }
}

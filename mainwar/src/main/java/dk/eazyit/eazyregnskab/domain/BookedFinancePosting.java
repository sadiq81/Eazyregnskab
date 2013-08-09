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

        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_LEGAL_ENTITY_FROM_DATE_TO_DATE, query = "select bp from BookedFinancePosting bp " +
                "WHERE bp.financeAccount.id in (select fp.id from FinanceAccount fp where fp.legalEntity = ?1) and bp.date >= ?2 and bp.date <= ?3"),

        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_PRIMO_FINANCE_POSTING_BY_ACCOUNT, query = "select bp from BookedFinancePosting bp " +
                "WHERE bp.financeAccount = ?1 and bp.date = ?2 and bp.bookedFinancePostingType = ?3 "),

        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_IN_YEAR, query = "select bp from BookedFinancePosting  bp " +
                "WHERE bp.financeAccount.legalEntity = ?1 and bp.date >= ?2 and bp.date <= ?3 "),

        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_INTERVAL_AND_DATE, query = "select bp from BookedFinancePosting  bp " +
                "WHERE bp.financeAccount in (?1) and bp.date >= ?2 and bp.date <= ?3 "),

        @NamedQuery(name = BookedFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_BOOKING_NUMBER, query = "select bp from BookedFinancePosting  bp " +
                "WHERE bp.financeAccount.legalEntity = ?1 and bp.date = ?2 and bp.bookingNumber = ?3 ")

})
@Table(name = "bookedfinanceposting")
public class BookedFinancePosting extends BaseEntity {

    public static final String QUERY_FIND_FINANCE_POSTING_BY_FINANCE_ACCOUNT = "BookedFinancePosting::findBookedFinancePostingByFinanceAccount";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_ACCOUNT_FROM_DATE_TO_DATE = "BookedFinancePosting::findBookedFinancePostingByFinanceAccountsFromDateToDate";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_LEGAL_ENTITY = "BookedFinancePosting::findBookedFinancePostingByLegalEntity";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_LEGAL_ENTITY_FROM_DATE_TO_DATE = "BookedFinancePosting::findBookedFinancePostingByLegalEntityFromDateToDate";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_VAT_TYPE = "BookedFinancePosting::findBookedFinancePostingByVatType";
    public static final String QUERY_FIND_PRIMO_FINANCE_POSTING_BY_ACCOUNT = "BookedFinancePosting::findPrimoFinancePostingByAccount";
    public static final String QUERY_FIND_FINANCE_POSTING_IN_YEAR = "BookedFinancePosting::findBookedFinancePostingInYear";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_INTERVAL_AND_DATE = "BookedFinancePosting::findBookedFinancePostingByIntervalAndDate";
    public static final String QUERY_FIND_FINANCE_POSTING_BY_BOOKING_NUMBER = "BookedFinancePosting::findBookedFinancePostingByBookingNumber";

    public static final String PARAM_BOOKED_FINANCE_POSTING_BOOKING_NUMBER = BookedFinancePosting.class.getName() + ".bookingNumber";

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

    public BookedFinancePosting setBookingNumber(int bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }


    public Date getDate() {
        return date;
    }

    public BookedFinancePosting setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getSimpleFormatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-YYYY");
        return sdf.format(getDate());
    }

    public String getText() {
        return text;
    }

    public BookedFinancePosting setText(String text) {
        this.text = text;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public BookedFinancePosting setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void removeVat(Double vat) {
        amount -= vat;
    }

    public FinanceAccount getFinanceAccount() {
        return financeAccount;
    }

    public BookedFinancePosting setFinanceAccount(FinanceAccount financeAccount) {
        this.financeAccount = financeAccount;
        return this;
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

    public BookedFinancePosting setSum(Double sum) {
        this.sum = sum;
        return this;
    }

    public BookedFinancePostingType getBookedFinancePostingType() {
        return bookedFinancePostingType;
    }

    public BookedFinancePosting setBookedFinancePostingType(BookedFinancePostingType bookedFinancePostingType) {
        this.bookedFinancePostingType = bookedFinancePostingType;
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
                .add("financeAccount", financeAccount.getName())
                .add("appUser", appUser != null ? appUser.getUsername() : null)
//                .add("vatType", vatType)
                .toString();
    }
}

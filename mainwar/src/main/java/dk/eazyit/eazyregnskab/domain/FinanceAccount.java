package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * @author
 */
@Entity
@NamedQueries({
        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, query = "select fa from FinanceAccount fa " +
                "WHERE fa.legalEntity = ?1 ORDER BY accountNumber ASC"),

        @NamedQuery(name = FinanceAccount.QUERY_FIND_SYSTEM_ACCOUNT_BY_LEGAL_ENTITY, query = "select fa from FinanceAccount fa " +
                "WHERE fa.legalEntity = ?1 and fa.financeAccountType =?2 "),

        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_LOWEST, query = "select f from FinanceAccount f " +
                "where f.legalEntity = ?1 and f.accountNumber = " +
                "(select min(fa.accountNumber) from FinanceAccount fa where fa.legalEntity = ?1)"),

        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_HIGHEST, query = "select f from FinanceAccount f " +
                "where f.legalEntity = ?1 and f.accountNumber = " +
                "(select max(fa.accountNumber) from FinanceAccount fa where fa.legalEntity = ?1)"),

        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_AND_NAME, query = "select fa from FinanceAccount fa " +
                "WHERE fa.legalEntity = ?1 and fa.name = ?2 ORDER BY accountNumber ASC"),

        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_AND_ACCOUNT_NUMBER, query = "select fa from FinanceAccount fa " +
                "WHERE fa.legalEntity = ?1 and fa.accountNumber = ?2 ORDER BY accountNumber ASC"),

        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_AND_FROM_ACCOUNT_TO_ACCOUNT, query = "select fa from FinanceAccount fa " +
                "WHERE fa.legalEntity = ?1 and fa.accountNumber >= ?2 and fa.accountNumber <= ?3 ORDER BY accountNumber ASC")
})
@Table(name = "financeaccount")
public class FinanceAccount extends BaseEntity {

    public static final String QUERY_FIND_BY_LEGAL_ENTITY = "FinanceAccount::findByLegalEntity";
    public static final String QUERY_FIND_SYSTEM_ACCOUNT_BY_LEGAL_ENTITY = "FinanceAccount::findSystemAccountByLegalEntity";
    public static final String QUERY_FIND_BY_LEGAL_ENTITY_LOWEST = "FinanceAccount::findByLegalEntityLowest";
    public static final String QUERY_FIND_BY_LEGAL_ENTITY_HIGHEST = "FinanceAccount::findByLegalEntityHighest";
    public static final String QUERY_FIND_BY_LEGAL_ENTITY_AND_NAME = "FinanceAccount::findByLegalEntityAndName";
    public static final String QUERY_FIND_BY_LEGAL_ENTITY_AND_ACCOUNT_NUMBER = "FinanceAccount::findByLegalEntityAndAccount";
    public static final String QUERY_FIND_BY_LEGAL_ENTITY_AND_FROM_ACCOUNT_TO_ACCOUNT = "FinanceAccount::findByLegalEntityAndAccountNumberFromAccountToAccount";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 100)
    private String name;

    @Column(unique = false, nullable = false, length = 50)
    private Integer accountNumber;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "vatType_id")
    private VatType vatType;

    @Column(unique = false, nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private FinanceAccountType financeAccountType;

    @OneToOne(optional = true)
    private FinanceAccount standardReverseFinanceAccount;

    @OneToOne(optional = true)
    private FinanceAccount sumFrom;

    @OneToOne(optional = true)
    private FinanceAccount sumTo;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "legalentity_id")
    private LegalEntity legalEntity;

    @Column
    private boolean locked;

    @Transient
    private Double sum;

    @Transient
    private Double sumCompare;

    @Column(nullable = false)
    private boolean inUse;

    public FinanceAccount() {
    }

    public FinanceAccount(String name, Integer accountNumber) {
        this.name = name;
        this.accountNumber = accountNumber;
    }

    public FinanceAccount(String name, Integer accountNumber, FinanceAccountType financeAccountType, LegalEntity legalEntity) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.financeAccountType = financeAccountType;
        this.legalEntity = legalEntity;
    }

    @Override
    public Long getId() {
        return id;
    }

    public FinanceAccount setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public VatType getVatType() {
        return vatType;
    }

    public void setVatType(VatType vatType) {
        this.vatType = vatType;
    }

    public FinanceAccountType getFinanceAccountType() {
        return financeAccountType;
    }

    public void setFinanceAccountType(FinanceAccountType financeAccountType) {
        this.financeAccountType = financeAccountType;
    }

    public FinanceAccount getStandardReverseFinanceAccount() {
        return standardReverseFinanceAccount;
    }

    public void setStandardReverseFinanceAccount(FinanceAccount standardReverseFinanceAccount) {
        this.standardReverseFinanceAccount = standardReverseFinanceAccount;
    }

    public FinanceAccount getSumFrom() {
        return sumFrom;
    }

    public void setSumFrom(FinanceAccount sumFrom) {
        this.sumFrom = sumFrom;
    }

    public FinanceAccount getSumTo() {
        return sumTo;
    }

    public void setSumTo(FinanceAccount sumTo) {
        this.sumTo = sumTo;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public FinanceAccount setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
        return this;
    }

    public Double getSum() {
        if (sum == null) return new Double(0);
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getSumCompare() {
        return sumCompare;
    }

    public void setSumCompare(Double sumCompare) {
        this.sumCompare = sumCompare;
    }

    public boolean isSystemAccount() {
        return financeAccountType.isSystem_account();
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("accountNumber", accountNumber)
//                .add("vatType", vatType)
                .add("financeAccountType", financeAccountType)
//                .add("standardReverseFinanceAccount", standardReverseFinanceAccount)
//                .add("legalEntity", legalEntity)
                .toString();
    }

    public boolean isBookable() {
        return financeAccountType != FinanceAccountType.HEADLINE && financeAccountType != FinanceAccountType.SUM && financeAccountType != FinanceAccountType.CATEGORY;
    }
}

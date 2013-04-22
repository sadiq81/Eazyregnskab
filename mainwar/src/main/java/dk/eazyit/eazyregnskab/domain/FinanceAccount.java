package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

/**
 * @author
 */
@Entity
@NamedQueries({
        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, query = "select fa from FinanceAccount fa " +
                "WHERE fa.legalEntity = ?1"),
        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_AND_NAME, query = "select fa from FinanceAccount fa " +
                "WHERE fa.legalEntity = ?1 and fa.name = ?2"),
        @NamedQuery(name = FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY_AND_ACCOUNT_NUMBER, query = "select fa from FinanceAccount fa " +
                "WHERE fa.legalEntity = ?1 and fa.accountNumber = ?2"),
})
@Table(name = "financeaccount")
public class FinanceAccount extends BaseEntity {

    public static final String QUERY_FIND_BY_LEGAL_ENTITY = "FinanceAccount::findByLegalEntity";
    public static final String QUERY_FIND_BY_LEGAL_ENTITY_AND_NAME = "FinanceAccount::findByLegalEntityAndName";
    public static final String QUERY_FIND_BY_LEGAL_ENTITY_AND_ACCOUNT_NUMBER = "FinanceAccount::findByLegalEntityAndAccountNumber";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String accountNumber;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "vatType_id")
    private VatType vatType;

    @Column(unique = false, nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private FinanceAccountType financeAccountType;

    @OneToOne(optional = true)
    private FinanceAccount standardReverseFinanceAccount;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "legalentity_id")
    private LegalEntity legalEntity;


    public FinanceAccount() {
    }

    public FinanceAccount(String name, String accountNumber) {
        this.name = name;
        this.accountNumber = accountNumber;
    }

    public FinanceAccount(String name, String accountNumber, FinanceAccountType financeAccountType, LegalEntity legalEntity) {
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
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

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public FinanceAccount setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
        return this;
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
}

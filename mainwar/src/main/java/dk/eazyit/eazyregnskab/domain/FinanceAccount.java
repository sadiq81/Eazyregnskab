package dk.eazyit.eazyregnskab.domain;

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
                "WHERE fa.legalEntity = ?1")
})
@Table(name = "financeaccount")
public class FinanceAccount extends BaseEntity {

    public static final String QUERY_FIND_BY_LEGAL_ENTITY = "FinanceAccount::findByLegalEntity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 50)
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
        return "FinanceAccount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", financeAccountType=" + financeAccountType +
                ", legalEntity=" + legalEntity.getName() +
                '}';
    }
}

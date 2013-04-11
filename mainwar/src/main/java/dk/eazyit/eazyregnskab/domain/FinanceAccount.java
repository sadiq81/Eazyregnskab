package dk.eazyit.eazyregnskab.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

/**
 * @author
 */
@Entity
@Table(name = "financeaccount")
public class FinanceAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String accountNumber;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "financeaccount_id")
    private VatType vatType;

    @Column(unique = false, nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private FinanceAccountType financeAccountType;

    @OneToMany
    private Set<FinancePosting> financePosting;

    public FinanceAccount() {
    }

    @Override
    public Long getId() {
        return id;
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

    public Set<FinancePosting> getFinancePosting() {
        return financePosting;
    }

    public void setFinancePosting(Set<FinancePosting> financePosting) {
        this.financePosting = financePosting;
    }

    @Override
    public String toString() {
        return "FinanceAccount{" +
                "financeAccountType=" + financeAccountType +
                ", vatType=" + vatType +
                ", accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

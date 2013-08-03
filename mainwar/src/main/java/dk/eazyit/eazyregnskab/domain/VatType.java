package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author
 */
@Entity
@NamedQueries({
        @NamedQuery(name = VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, query = "select vt from VatType vt " +
                "WHERE vt.legalEntity = ?1"),
        @NamedQuery(name = VatType.QUERY_FIND_VATTYPE_BY_NAME_AND_LEGAL_ENTITY, query = "select vt from VatType vt " +
                "WHERE vt.name = ?1 AND vt.legalEntity = ?2")
})
@Table(name = "vattype")
public class VatType extends BaseEntity {

    public static final String QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY = "VatType::findVatTypeByLegalEntity";
    public static final String QUERY_FIND_VATTYPE_BY_NAME_AND_LEGAL_ENTITY = "VatType::FindVatTypeByNameAndLegalEntity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 50)
    private String name;

    @Column(unique = false, nullable = false, precision = 5, scale = 2)
    private Double percentage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "financeAccount_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private FinanceAccount financeAccount;

    @ManyToOne(optional = true)
    @JoinColumn(name = "financeAccount_reverse_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private FinanceAccount financeAccountReverse;

    @ManyToOne(optional = false)
    @JoinColumn(name = "legalentity_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LegalEntity legalEntity;

    @Column(nullable = false)
    private boolean inUse;

    public VatType() {
    }

    public VatType(String name, Double percentage, LegalEntity legalEntity, FinanceAccount financeAccount) {
        this.name = name;
        this.percentage = percentage;
        this.legalEntity = legalEntity;
        this.financeAccount = financeAccount;
    }

    public VatType(String name, Double percentage, LegalEntity legalEntity, FinanceAccount financeAccount, FinanceAccount financeAccountReverse) {
        this.name = name;
        this.percentage = percentage;
        this.legalEntity = legalEntity;
        this.financeAccount = financeAccount;
        this.financeAccountReverse = financeAccountReverse;
    }

    public VatType(String name, Double percentage, FinanceAccount financeAccount, FinanceAccount financeAccountReverse, LegalEntity legalEntity) {
        this.name = name;
        this.percentage = percentage;
        this.financeAccount = financeAccount;
        this.financeAccountReverse = financeAccountReverse;
        this.legalEntity = legalEntity;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
    }

    public FinanceAccount getFinanceAccount() {
        return financeAccount;
    }

    public VatType setFinanceAccount(FinanceAccount financeAccount) {
        this.financeAccount = financeAccount;
        return this;
    }

    public FinanceAccount getFinanceAccountReverse() {
        return financeAccountReverse;
    }

    public VatType setFinanceAccountReverse(FinanceAccount financeAccountReverse) {
        this.financeAccountReverse = financeAccountReverse;
        return this;
    }

    public boolean isReverse() {
        return financeAccount != null && financeAccountReverse != null;
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
                .add("percentage", percentage)
//                .add("legalEntity", legalEntity)
                .toString();
    }
}

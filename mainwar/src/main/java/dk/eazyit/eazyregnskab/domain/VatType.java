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
                "WHERE vt.legalEntity = ?1")
})
@Table(name = "vattype")
public class VatType extends BaseEntity {

    public static final String QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY = "VatType::findVatTypeByLegalEntity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(unique = false, nullable = false, precision = 5, scale = 2)
    private Double percentage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "legalentity_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LegalEntity legalEntity;

    public VatType() {
    }

    public VatType(String name, Double percentage, LegalEntity legalEntity) {
        this.name = name;
        this.percentage = percentage;
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

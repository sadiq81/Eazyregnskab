package dk.eazyit.eazyregnskab.domain;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author
 */
@Entity
@Table(name = "vattype")
public class VatType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(unique = false, nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    public VatType() {

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

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "VatType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}

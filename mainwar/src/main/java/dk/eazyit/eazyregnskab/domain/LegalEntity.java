package dk.eazyit.eazyregnskab.domain;

import javax.persistence.*;
import java.util.Set;


/**
 * @author EazyIT
 */
@Entity
@Table(name = "legalentity")
public class LegalEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = true, length = 25)
    private String legalIdentification;

    @Column(unique = false, nullable = true, length = 200)
    private String address;

    @Column(unique = false, nullable = true, length = 20)
    private String postalCode;

    @Column(unique = false, nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(unique = false, nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private MoneyCurrency moneyCurrency;

    @OneToMany(mappedBy = "legalEntity", fetch = FetchType.EAGER)
    private Set<LegalEntityAccess> legalEntityAccess;

    public LegalEntity() {
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

    public String getLegalIdentification() {
        return legalIdentification;
    }

    public void setLegalIdentification(String legalIdentification) {
        this.legalIdentification = legalIdentification;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public MoneyCurrency getMoneyCurrency() {
        return moneyCurrency;
    }

    public void setMoneyCurrency(MoneyCurrency moneyCurrency) {
        this.moneyCurrency = moneyCurrency;
    }

    public Set<LegalEntityAccess> getLegalEntityAccess() {
        return legalEntityAccess;
    }

    public void setLegalEntityAccess(Set<LegalEntityAccess> legalEntityAccess) {
        this.legalEntityAccess = legalEntityAccess;
    }
}

package dk.eazyit.eazyregnskab.domain;

import javax.persistence.*;


/**
 * @author EazyIT
 */
@Entity
@NamedQueries({
        @NamedQuery(name = LegalEntity.QUERY_FIND_LEGAL_ENTITY_BY_USER, query = "select le from LegalEntity le, LegalEntityAccess lea, AppUser u " +
                "WHERE le = lea.legalEntity AND lea.appUser = ?1")
})
@Table(name = "legalentity")
public class LegalEntity extends BaseEntity {

    public static final String QUERY_FIND_LEGAL_ENTITY_BY_USER = "LegalEntity::findLegalEntityByUser";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 50)
    private String name;

    @Column(unique = false, nullable = true, length = 25)
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

    public LegalEntity() {
    }

    public LegalEntity(String name, String legalIdentification, String address, String postalCode, Country country, MoneyCurrency moneyCurrency) {
        this.name = name;
        this.legalIdentification = legalIdentification;
        this.address = address;
        this.postalCode = postalCode;
        this.country = country;
        this.moneyCurrency = moneyCurrency;
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
}

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
        @NamedQuery(name = LegalEntityAccess.QUERY_FIND_LEGAL_ENTITY_ACCESS_BY_LEGAL_ENTITY, query = "select a from LegalEntityAccess a where a.legalEntity = ?1")
})
@Table(name = "legalentityaccess")
public class LegalEntityAccess extends BaseEntity {

    public static final String QUERY_FIND_LEGAL_ENTITY_ACCESS_BY_LEGAL_ENTITY = "legalEntityAccess::FindLegalEntityAccessByLegalEntity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "legalentity_id")
    private LegalEntity legalEntity;

    public LegalEntityAccess() {
    }

    public LegalEntityAccess(AppUser appUser, LegalEntity legalEntity) {
        this.appUser = appUser;
        this.legalEntity = legalEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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
//                .add("appUser", appUser)
//                .add("legalEntity", legalEntity)
                .toString();
    }
}

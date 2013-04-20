package dk.eazyit.eazyregnskab.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Trifork
 */
@Entity
@NamedQueries({
        @NamedQuery(name = AppUserRole.QUERY_FIND_BY_USER, query = "select a from AppUserRole a where a.appUser= ?1")
})
@Table(name = "user_roles")
public class AppUserRole extends BaseEntity {

    public static final String QUERY_FIND_BY_USER = "AppUserRole::findByUser";

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @Column(unique = false, nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    public AppUserRole() {
    }

    public AppUserRole(AppUser appUser, Authority authority) {
        this.appUser = appUser;
        this.authority = authority;
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

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "AppUserRole{" +
                "id=" + id +
                ", appUser=" + appUser.getUsername() +
                ", authority=" + authority.getAuthority() +
                '}';
    }
}

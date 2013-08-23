package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

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

    @Column(unique = true, nullable = false, length = 25)
    private String username;

    @Column(unique = false, nullable = false, length = 25)
    private Role role;

    public AppUserRole() {
    }

    public AppUserRole(AppUser appUser, Role role) {
        this.appUser = appUser;
        this.username = appUser.getUsername();
        this.role = role;
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
        this.username = appUser.getUsername();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
//                .add("appUser", appUser)
//                .add("authority", authority)
                .toString();
    }
}

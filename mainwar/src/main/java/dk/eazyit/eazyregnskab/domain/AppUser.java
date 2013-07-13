package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Trifork
 */
@Entity
@NamedQueries({
        @NamedQuery(name = AppUser.QUERY_FIND_BY_USER_NAME, query = "select a from AppUser a where a.username = ?1")
})
@Table(name = "users")
public class AppUser extends BaseEntity {

    public static final String QUERY_FIND_BY_USER_NAME = "AppUser::findByUserName";

    public static final String ATTRIBUTE_NAME = AppUser.class.getName();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false, length = 25)
    private String username;
    @Column(unique = false, nullable = false, length = 256)
    private String password;
    @Column(unique = false, nullable = false)
    private boolean enabled;
    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    private Set<AppUserRole> appUserRoles;
    @Column
    private String email;
    @Column
    private String verificationUUID;

    public AppUser() {
    }

    public AppUser(String username, String password, Boolean enabled, String email, String verificationUUID) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email =email;
        this.verificationUUID = verificationUUID;
    }

    public AppUser(String username, String password, boolean enabled, Set<AppUserRole> appUserRoles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.appUserRoles = appUserRoles;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<AppUserRole> getAppUserRoles() {
        return appUserRoles;
    }

    public void setAppUserRoles(Set<AppUserRole> appUserRoles) {
        this.appUserRoles = appUserRoles;
    }

    public boolean isActive() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationUUID() {
        return verificationUUID;
    }

    public void setVerificationUUID(String verificationUUID) {
        this.verificationUUID = verificationUUID;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("password", password)
                .add("enabled", enabled)
//                .add("appUserRoles", appUserRoles)
                .toString();
    }
}

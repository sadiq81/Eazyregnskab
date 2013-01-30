package dk.eazyit.eazyregnskab.domain;

import java.io.Serializable;

/**
 * @author Trifork
 */
public class AppUser implements Serializable {

    public AppUser() {
    }

    private long id;
    private String username;
    private String password;
    private String subscriptionId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}

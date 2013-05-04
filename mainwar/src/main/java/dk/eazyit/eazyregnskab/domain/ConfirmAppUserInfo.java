package dk.eazyit.eazyregnskab.domain;

import java.io.Serializable;

/**
 * @author
 */
public class ConfirmAppUserInfo implements Serializable {

    private String username = null;
    private String password = null;
    private String UUID = null;

    public ConfirmAppUserInfo() {
    }

    public ConfirmAppUserInfo(String UUID) {
        this.UUID = UUID;
    }

    public ConfirmAppUserInfo(String username, String password, String UUID) {
        this.username = username;
        this.password = password;
        this.UUID = UUID;
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

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}

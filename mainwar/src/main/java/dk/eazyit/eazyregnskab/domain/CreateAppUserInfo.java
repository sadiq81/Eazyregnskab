package dk.eazyit.eazyregnskab.domain;

import java.io.Serializable;

/**
 * @author
 */
public class CreateAppUserInfo implements Serializable {

    private String username = null;
    private String password = null;
    private String repeat_password = null;
    private String email = null;

    public CreateAppUserInfo() {
    }

    public CreateAppUserInfo(String username, String password, String repeat_password, String email) {
        this.username = username;
        this.password = password;
        this.repeat_password = repeat_password;
        this.email = email;
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

    public String getRepeat_password() {
        return repeat_password;
    }

    public void setRepeat_password(String repeat_password) {
        this.repeat_password = repeat_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

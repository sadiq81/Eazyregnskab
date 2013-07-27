package dk.eazyit.eazyregnskab.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Eazy IT
 */
@Entity
@Table(name = "persistent_logins")
public class RememberMe extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(columnDefinition = "varchar(50) NOT NULL")
    String username;
    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    Timestamp last_used;
    @Column(columnDefinition = "varchar(64) NOT NULL")
    String series;
    @Column(columnDefinition = "varchar(64) NOT NULL")
    String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getLast_used() {
        return last_used;
    }

    public void setLast_used(Timestamp last_used) {
        this.last_used = last_used;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /*
    * CREATE TABLE IF NOT EXISTS `persistent_logins` (
      `username` varchar(50) NOT NULL,
      `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      `series` varchar(64) NOT NULL,
      `token` varchar(64) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=latin1
    * */
}

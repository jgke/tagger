package fi.jgke.tagger.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * A user.
 *
 * @author jgke
 */
@Entity
@Table(name = "users")
public class User extends AbstractPersistable<Long> {

    @Column(name = "username")
    private String username;
    @JsonIgnore
    @Column(name = "email")
    private String email;
    @JsonIgnore
    @Column(name = "password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

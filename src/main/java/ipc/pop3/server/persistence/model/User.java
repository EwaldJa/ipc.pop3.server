package ipc.pop3.server.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    private String username;
    private String password;

    protected User () {}

    public User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User (User original) {
        this.username = new String(original.username);
        this.password = new String(original.password);
    }

    @Override
    public String toString() {
        return String.format(
                "User[username='%s']",
                username);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return username.equals(that.username);
    }
}

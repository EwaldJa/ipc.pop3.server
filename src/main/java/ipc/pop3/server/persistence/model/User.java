package ipc.pop3.server.persistence.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user")
public class User {

    @Id
    private String username;
    private String password;
    private String usersalt;

    protected User () {}

    public User (String username, String password, String usersalt) {
        this.username = username;
        this.password = password;
        this.usersalt = usersalt;
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

    public String getUsersalt() {
        return usersalt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return username.equals(that.username);
    }
}

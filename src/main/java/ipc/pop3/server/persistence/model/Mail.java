package ipc.pop3.server.persistence.model;

import javax.persistence.*;

@Table(name = "mail")
public class Mail {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String object;
    private String message;
    private String sender;
    private User recipient;

    protected Mail () {}

    public Mail (String object, String message, String sender, User recipient) {
        this.object = object;
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return String.format(
                "Mail[id=%d, object='%s', message='%s', sender='%s', recipient='%s']",
                id, object, message, sender, recipient.getUsername());
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getObject() {
        return object;
    }

    public String getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail that = (Mail) o;
        return id.equals(that.id);
    }
}

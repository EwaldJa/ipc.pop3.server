package ipc.pop3.server.persistence.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "mail")
public class Mail {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String subject;
    private String message;
    private String sender;
    private User recipient;
    private Timestamp date;

    protected Mail () {}

    public Mail (String subject, String message, String sender, User recipient) {
        this.subject = subject;
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return String.format(
                "Mail[id=%d, subject='%s', message='%s', sender='%s', recipient='%s', date='%s']",
                id, subject, message, sender, recipient.getUsername(), date.toString());
    }

    public String toPOP3String() {
        String headers = String.format(
                "From: <%s>\r\nTo: <'%s'>\r\nSubject: '%s'\r\nDate: '%s'\r\nMessage-ID: <'%s'>\r\n",
                sender, recipient.getUsername(), subject, date.toString(), sender);
        String messageBody = "";
            if (this.message.length() > 998) {

            }
            else {
                messageBody = this.message;
            }

        return headers + "\r\n" + message + ".\r\n";

    }

    public int getSize() {
        return this.toPOP3String().length();
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public String getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public Timestamp getDate() { return date; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail that = (Mail) o;
        return id.equals(that.id);
    }
}

package ipc.pop3.server.persistence.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

    private boolean toBeDeleted;

    protected Mail () {}

    public Mail(String subject, String message, String sender, User recipient) {
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
        StringBuffer messageBuffer = new StringBuffer();
        String remainingMessage = this.message;
            if (this.message.length() > 998) {
                while (remainingMessage.length() > 998) {
                    messageBuffer.append(remainingMessage, 0, 998);
                    remainingMessage = remainingMessage.substring(998);
                }
            }
            messageBuffer.append(remainingMessage);

        return headers + "\r\n" + messageBuffer.toString() + ".\r\n";

    }

    public static int getSize(List<Mail> mails) {
        int somme = 0;
        for (Mail mail : mails) {
            somme += mail.getSize();
        }
        return somme;
    }

    public int getSize() {
        return this.toPOP3String().getBytes().length;
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

    public boolean isToBeDeleted() { return toBeDeleted; }

    public void setToBeDeleted(boolean toBeDeleted) { this.toBeDeleted = toBeDeleted; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail that = (Mail) o;
        return id.equals(that.id);
    }
}

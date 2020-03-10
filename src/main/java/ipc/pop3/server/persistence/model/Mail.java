package ipc.pop3.server.persistence.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "mail")
public class Mail {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String subject;
    private String message;
    private String sender;
    @ManyToOne
    @JoinColumn(name = "recipient", referencedColumnName = "username")
    private User recipient;
    private Timestamp date;

    protected Mail () {}

    public Mail(String subject, String message, String sender, User recipient) {
        this.subject = subject;
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Mail(Mail original) {
        this.subject = new String(original.subject);
        this.message = new String(original.message);
        this.sender = new String(original.sender);
        this.recipient = new User(original.recipient);
        this.date = new Timestamp(original.date.getTime());
        this.id = original.id;
    }

    public String toPOP3String() {
        String headers = String.format(
                "From: <%s>\r\nTo: <'%s'>\r\nSubject: '%s'\r\nDate: '%s'\r\nMessage-ID: <'%d'@ewaldetlucas.ipc>\r\n",
                sender, recipient.getUsername(), subject, date.toString(), id);
        StringBuffer messageBuffer = new StringBuffer();
        String remainingMessage = this.message;
            if (this.message.length() > 998) {
                while (remainingMessage.length() > 998) {
                    messageBuffer.append(remainingMessage, 0, 998);
                    remainingMessage = remainingMessage.substring(998); } }
            messageBuffer.append(remainingMessage);
        return headers + "\r\n" + messageBuffer.toString() + ".\r\n";
    }

    public String toPOP3ListString(int messageNumber) {
        return String.format(
                "%d %d",
                messageNumber, getSize());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail that = (Mail) o;
        return id.equals(that.id);
    }
}

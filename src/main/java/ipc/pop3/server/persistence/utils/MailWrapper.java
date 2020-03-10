package ipc.pop3.server.persistence.utils;

import ipc.pop3.server.persistence.model.Mail;

public class MailWrapper {

    private Mail mail;
    private boolean toBeDeleted;

    public MailWrapper(Mail mail, boolean toBeDeleted) {
        this.mail = new Mail(mail);
        this.toBeDeleted = toBeDeleted;
    }

    public Mail getMail() { return mail; }
    public boolean isToBeDeleted() { return toBeDeleted; }


    public void setMail(Mail mail) { this.mail = mail; }
    public void setToBeDeleted(boolean toBeDeleted) { this.toBeDeleted = toBeDeleted; }
}

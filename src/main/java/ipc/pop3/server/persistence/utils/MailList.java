package ipc.pop3.server.persistence.utils;

import ipc.pop3.server.persistence.model.Mail;
import ipc.pop3.server.utils.exceptions.InvalidMailNumberException;
import ipc.pop3.server.utils.exceptions.MarkedAsDeletedMessageException;
import ipc.pop3.server.utils.exceptions.NoSuchMessageException;

import java.util.ArrayList;
import java.util.List;

public class MailList {

    private ArrayList<Mail> originalList;
    private ArrayList<Mail> usageList;

    public MailList(List<Mail> mailList) {
        this.originalList = new ArrayList<>();
        this.usageList = new ArrayList<>();
        mailList.forEach(mail->{originalList.add(new Mail(mail));usageList.add(new Mail(mail));});
    }

    public void reset() {
        usageList = new ArrayList<>();
        originalList.forEach(mail->usageList.add(new Mail(mail)));
    }

    public int getOctetSize() {
        int somme = 0;
        for (Mail mail : usageList) { somme += mail.getSize(); }
        return somme;
    }

    public int getMailTotalNumber() {
        return usageList.size();
    }

    public ArrayList<Mail> getToBeDeletedList() {
        ArrayList<Mail> toBeDeletedList = new ArrayList<>();
        usageList.forEach(mail->{if(mail.isToBeDeleted()){toBeDeletedList.add(mail);}});
        return toBeDeletedList;
    }

    public String toPOP3StatString() {
        return String.format(
                "%d %d",
                getMailTotalNumber(), getOctetSize());
    }

    public Mail getMail(int mailNumber) throws NoSuchMessageException, MarkedAsDeletedMessageException, InvalidMailNumberException {
        if (mailNumber < 0) { throw new InvalidMailNumberException("Mail number must not be negative."); }
        if (mailNumber >= usageList.size()) { throw new NoSuchMessageException("No such message : " + mailNumber); }
        if (usageList.get(mailNumber).isToBeDeleted()) { throw new MarkedAsDeletedMessageException("Message marked as deleted."); }
        return usageList.get(mailNumber);
    }
}

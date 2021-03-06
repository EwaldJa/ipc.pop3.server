package ipc.pop3.server.persistence.service;

import ipc.pop3.server.persistence.dao.MailRepository;
import ipc.pop3.server.persistence.model.Mail;
import ipc.pop3.server.persistence.model.User;
import ipc.pop3.server.persistence.utils.MailList;
import ipc.pop3.server.utils.exceptions.EmptyMailException;
import ipc.pop3.server.utils.exceptions.InvalidSenderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MailService {

    @Autowired
    private MailRepository mailRepository;

    synchronized public MailList findByUser(User currUser) {
        try {
            return new MailList(mailRepository.findByRecipient(currUser)); }
        catch (InvalidDataAccessResourceUsageException e) { return new MailList(new ArrayList<>());}
    }

    synchronized public Mail createMail(String subject, String message, String sender, User recipient) throws EmptyMailException, InvalidSenderException {
        if ((subject==null || "".equals(subject) || "".equals(subject.replace(" ", ""))) && (message==null || "".equals(message) || "".equals(message.replace(" ", "")))) {
            throw new EmptyMailException("Both subject and message of the mail are empty or null.");
        }
        if (sender==null || "".equals(sender) || "".equals(sender.replace(" ", ""))) {
            throw new InvalidSenderException("The sender is not valid for this mail : '" + sender + "'.");
        }
        Mail newMail = new Mail(subject, message, sender, recipient);
        return mailRepository.save(newMail);
    }

    synchronized public void update(MailList mailList) {
        mailRepository.deleteAll(mailList.getToBeDeletedList());
    }
}

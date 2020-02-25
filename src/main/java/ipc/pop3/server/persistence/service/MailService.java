package ipc.pop3.server.persistence.service;

import ipc.pop3.server.persistence.dao.MailRepository;
import ipc.pop3.server.persistence.model.Mail;
import ipc.pop3.server.persistence.model.User;
import ipc.pop3.server.utils.exceptions.EmptyMailException;
import ipc.pop3.server.utils.exceptions.InvalidSenderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {

    @Autowired
    private MailRepository mailRepository;

    synchronized public List<Mail> findByUser(User currUser) {
        List<Mail> mailList = mailRepository.findByRecipient(currUser);
        //TODO: headers ?
        return mailList;
    }

    synchronized public Mail createMail(String subject, String message, String sender, User recipient) throws EmptyMailException, InvalidSenderException {
        if ((subject==null || "".equals(subject) || "".equals(subject.replace(" ", ""))) && (message==null || "".equals(message) || "".equals(message.replace(" ", "")))) {
            throw new EmptyMailException("Both subject and message of the mail are empty or null.");
        }
        if (sender==null || "".equals(sender) || "".equals(sender.replace(" ", ""))) {
            throw new InvalidSenderException("The sender is not valid for this mail : '" + sender + "'.");
        }
        //TODO: create mail
        //Mail newMail = new Mail();
        return null;
    }
}

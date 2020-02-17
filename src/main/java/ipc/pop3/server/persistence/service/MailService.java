package ipc.pop3.server.persistence.service;

import ipc.pop3.server.persistence.dao.MailRepository;
import ipc.pop3.server.persistence.model.Mail;
import ipc.pop3.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {

    @Autowired
    private MailRepository mailRepository;

    public List<Mail> findByUser(User currUser) {
        List<Mail> mailList = mailRepository.findByRecipient(currUser);
        //TODO: headers ?
        return mailList;
    }
}

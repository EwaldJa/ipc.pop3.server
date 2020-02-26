package ipc.pop3.server.persistence.dao;

import ipc.pop3.server.persistence.model.Mail;
import ipc.pop3.server.persistence.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MailRepository extends CrudRepository<Mail, Long> {

    Mail findById(long id);
    List<Mail> findByRecipient(User recipient);

    @Override
    void deleteAll(Iterable<? extends Mail> iterable);
}

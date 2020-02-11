package ipc.pop3.server.persistence.dao;

import ipc.pop3.server.persistence.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}

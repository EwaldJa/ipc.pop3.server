package ipc.pop3.server.persistence.dao;

import ipc.pop3.server.persistence.model.Configuration;
import ipc.pop3.server.persistence.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfigurationRepository extends CrudRepository<Configuration, String> {

    Configuration findByConfiguration(String configuration);

    List<Configuration> findAll();
}

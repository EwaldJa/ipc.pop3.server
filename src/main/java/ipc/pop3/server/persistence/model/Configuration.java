package ipc.pop3.server.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuration")
public class Configuration {

    @Id
    private String configuration;
    private String value;

    protected Configuration() {}

    public Configuration(String configuration, String value) {
        this.configuration = configuration;
        this.value = value;
    }

    public Configuration(Configuration original) {
        this.configuration = new String(original.configuration);
        this.value = new String(original.value);
    }

    @Override
    public String toString() {
        return String.format(
                "Configuration[configuration='%s', value='%s']",
                configuration, value);
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return (configuration.equals(that.configuration) && value.equals(that.value));
    }
}

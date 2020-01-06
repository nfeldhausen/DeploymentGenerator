package de.th.bingen.master.backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "configuration")
public class DatabaseConfigurations {
    private List<DatabaseConfiguration> databases;

    public List<DatabaseConfiguration> getDatabases() {
        return databases;
    }

    public void setDatabases(List<DatabaseConfiguration> databases) {
        this.databases = databases;
    }
}

package com.alexshin.weatherapp.util;

import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;

public final class MigrationUtil {

    private MigrationUtil() {
        throw new UnsupportedOperationException();
    }

    public static void runFlywayMigration(Configuration dsConfig) {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        dsConfig.getProperty("hibernate.connection.url"),
                        dsConfig.getProperty("hibernate.connection.username"),
                        dsConfig.getProperty("hibernate.connection.password")
                )
                .load();
        flyway.migrate();
    }


}

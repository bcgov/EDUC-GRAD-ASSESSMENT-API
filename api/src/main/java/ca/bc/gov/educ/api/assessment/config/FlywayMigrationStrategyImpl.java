package ca.bc.gov.educ.api.assessment.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FlywayMigrationStrategyImpl implements FlywayMigrationStrategy {

    @Override
    public void migrate(Flyway flyway) {
        if (!flyway.validateWithResult().validationSuccessful) {
            flyway.repair();
        }

        log.info("\n");
        log.info("************FLYWAY-MIGRATE**********");
        flyway.migrate();
        log.info("************FLYWAY-MIGRATE-END**********\n\n");
    }
}

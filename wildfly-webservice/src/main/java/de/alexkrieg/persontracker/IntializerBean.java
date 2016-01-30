package de.alexkrieg.persontracker;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;

@Startup
@Singleton
public class IntializerBean {

    @Inject
    private Logger log;

    @Resource(lookup="java:/PostgresDS")
    private DataSource ds;
    
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NEVER)
    protected void flyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(ds);    
        flyway.setSchemas("public");
        flyway.setLocations("db.migration"); 
        MigrationInfoService migrationInfoService = flyway.info();
        log.info("Starting db migrations ... ("+migrationInfoService.pending().length+" pending)" );
        flyway.migrate();
        
    }
}

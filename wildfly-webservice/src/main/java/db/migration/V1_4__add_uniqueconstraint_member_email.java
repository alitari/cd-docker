package db.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

public class V1_4__add_uniqueconstraint_member_email implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
     // @formatter:off
        String sql = "ALTER TABLE member ADD CONSTRAINT uniqueEmail UNIQUE (email);";
     // @formatter:on

        PreparedStatement statement = connection.prepareStatement(sql);

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }

}

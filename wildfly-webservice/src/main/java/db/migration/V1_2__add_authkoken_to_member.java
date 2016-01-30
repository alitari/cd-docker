package db.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

public class V1_2__add_authkoken_to_member implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
        String sql = "ALTER TABLE Member ADD COLUMN authtoken varchar(40);";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }

}

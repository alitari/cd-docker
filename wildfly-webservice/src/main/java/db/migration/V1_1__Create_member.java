package db.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

public class V1_1__Create_member implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
        String sql = "CREATE SEQUENCE member_id_seq;"
                + "CREATE TABLE Member "
                + "( id integer CONSTRAINT firstkey PRIMARY KEY DEFAULT nextval('member_id_seq'),"
                + "  email varchar(40) NOT NULL,"
                + "  password varchar(13));"
                + "ALTER SEQUENCE member_id_seq OWNED BY Member.id";
        PreparedStatement statement = connection.prepareStatement(sql);

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }

}

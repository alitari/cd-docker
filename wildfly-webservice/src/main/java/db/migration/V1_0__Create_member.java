package db.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

public class V1_0__Create_member implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
        String sql = "CREATE SEQUENCE member_id_seq;"
                + "CREATE TABLE Member "
                + "( id integer CONSTRAINT firstkey PRIMARY KEY DEFAULT nextval('member_id_seq'),"
                + "  name varchar(26) NOT NULL,"
                + "  email varchar(40) NOT NULL,"
                + "  phone_number varchar(13),"
                + "  address varchar(40));"
                + "ALTER SEQUENCE member_id_seq OWNED BY Member.id";
        PreparedStatement statement = connection.prepareStatement(sql);

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }

}

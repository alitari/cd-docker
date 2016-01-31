package db.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

public class V1_3__Create_group implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
     // @formatter:off
        String sql = "CREATE SEQUENCE group_id_seq;"
                + "CREATE TABLE TGroup "
                + "( group_id integer CONSTRAINT groupkey PRIMARY KEY DEFAULT nextval('group_id_seq'),"
                + "  name varchar(40) NOT NULL UNIQUE"
                + " );"
                + "ALTER SEQUENCE group_id_seq OWNED BY TGroup.group_id;"
                + "ALTER TABLE Member RENAME COLUMN id TO member_id;"
                + "CREATE TABLE member_group "
                + "( member_id integer NOT NULL,"
                + "  group_id integer NOT NULL);";
     // @formatter:on

        PreparedStatement statement = connection.prepareStatement(sql);

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }

}

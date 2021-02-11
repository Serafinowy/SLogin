package me.serafin.slogin.database;

import java.io.File;
import java.sql.*;

public class SQLite implements DataBase {

    private Connection connection;
    private final File file;

    public SQLite(File file){
        this.file = file;
    }

    @Override
    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String URL = "jdbc:sqlite:" + file;
            connection = DriverManager.getConnection(URL);
        }
    }

    @Override
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public void update(String command, String... params) throws SQLException {
        openConnection();
        PreparedStatement statement = connection.prepareStatement(command);
        for(int i = 0; i<params.length; i++) {
            statement.setString(i+1, params[i]);
        }
        statement.executeUpdate();
    }

    @Override
    public ResultSet query(String command, String... params) throws SQLException {
        openConnection();
        PreparedStatement statement = connection.prepareStatement(command);
        for(int i = 0; i<params.length; i++) {
            statement.setString(i+1, params[i]);
        }
        return statement.executeQuery();
    }

    @Override
    public void createTableIfNotExist() throws SQLException {
        this.update("CREATE TABLE IF NOT EXISTS `slogin_accounts`" +
                "(`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`name` TEXT NOT NULL, " +
                "`password` VARCHAR(255) NOT NULL, " +
                "`email` VARCHAR(255) NULL, " +
                "`registerIP` TEXT NOT NULL, " +
                "`registerDate` BIGINT NOT NULL, " +
                "`lastLoginIP` TEXT NOT NULL, " +
                "`lastLoginDate` BIGINT NOT NULL)");
    }
}

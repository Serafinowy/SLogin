package me.serafin.slogin.database;

import java.io.File;
import java.sql.*;

public class SQLite implements DataBase {

    Connection connection;
    File file;

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
    public void update(String command) throws SQLException {
        openConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(command);
    }

    @Override
    public ResultSet query(String command) throws SQLException {
        openConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(command);
    }
}

package me.serafin.slogin.database;

import me.serafin.slogin.managers.ConfigManager;

import java.sql.*;

public class MYSQL implements DataBase {

    Connection connection;
    ConfigManager config;

    public MYSQL(ConfigManager config){
        this.config = config;
    }

    @Override
    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String URL = "jdbc:mysql://" + config.MYSQL_HOST + ":"
                    + config.MYSQL_PORT + "/"
                    + config.MYSQL_DATABASE;
            String USER = config.MYSQL_USER;
            String PASS = config.MYSQL_PASS;
            connection = DriverManager.getConnection(URL, USER, PASS);
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

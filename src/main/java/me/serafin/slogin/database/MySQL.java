package me.serafin.slogin.database;

import me.serafin.slogin.managers.ConfigManager;

import java.sql.*;

public class MySQL implements DataBase {

    Connection connection;
    ConfigManager config;

    public MySQL(ConfigManager config){
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
}

package me.serafin.slogin.database;

import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.models.Config;

import java.sql.*;

public final class MySQL implements DataBase {

    private final ConfigManager configManager;
    private Connection connection;

    public MySQL(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void openConnection() throws SQLException {
        Config config = configManager.getConfig();
        if (connection == null || connection.isClosed() || !connection.isValid(3)) {
            String URL = "jdbc:mysql://" + config.MYSQL_HOST + ":"
                    + config.MYSQL_PORT + "/"
                    + config.MYSQL_DATABASE + "?"
                    + config.MYSQL_PROPERTIES;
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
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        statement.executeUpdate();
    }

    @Override
    public ResultSet query(String command, String... params) throws SQLException {
        openConnection();
        PreparedStatement statement = connection.prepareStatement(command);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        return statement.executeQuery();
    }
}

package me.serafin.slogin.database;

import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.models.Config;

import java.sql.*;

public final class MySQL implements SQL {

    private final ConfigManager configManager;
    private Connection connection;

    public MySQL(ConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * Opens database connection
     *
     * @throws SQLException if connection can not be opened
     */
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

    /**
     * Closes database connection
     *
     * @throws SQLException if connection can not be closed
     */
    @Override
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Send SQL update prepared statement. Symbol '?' is replaced by parameter
     *
     * @param command sql command string
     * @param params  parameters table
     * @throws SQLException if statement can not be send
     */
    @Override
    public void update(String command, String... params) throws SQLException {
        openConnection();
        PreparedStatement statement = connection.prepareStatement(command);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        statement.executeUpdate();
    }

    /**
     * Send SQL query prepared statement. Symbol '?' is replaced by parameter
     *
     * @param command sql command string
     * @param params  parameters table
     * @return result in ResultSet object
     * @throws SQLException if statement can not be send
     */
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

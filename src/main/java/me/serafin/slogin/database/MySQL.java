package me.serafin.slogin.database;

import me.serafin.slogin.managers.ConfigManager;

import java.sql.*;

public final class MySQL implements DataBase {

    private final ConfigManager config;
    private Connection connection;

    public MySQL(ConfigManager config) {
        this.config = config;
    }

    @Override
    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(3)) {
            String URL = "jdbc:mysql://" + config.getMYSQL_HOST() + ":"
                    + config.getMYSQL_PORT() + "/"
                    + config.getMYSQL_DATABASE() + "?"
                    + config.getMYSQL_PROPERTIES();
            String USER = config.getMYSQL_USER();
            String PASS = config.getMYSQL_PASS();
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

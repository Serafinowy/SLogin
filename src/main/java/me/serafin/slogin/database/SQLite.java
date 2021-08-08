package me.serafin.slogin.database;

import java.io.File;
import java.sql.*;

public final class SQLite implements SQL {

    private final File file;
    private Connection connection;

    public SQLite(File file) {
        this.file = file;
    }

    /**
     * Opens database connection
     *
     * @throws SQLException if connection can not be opened
     */
    @Override
    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(3)) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String URL = "jdbc:sqlite:" + file;
            connection = DriverManager.getConnection(URL);
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

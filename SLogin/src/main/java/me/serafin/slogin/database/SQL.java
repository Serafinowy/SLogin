package me.serafin.slogin.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQL {

    /**
     * Opens database connection
     *
     * @throws SQLException if connection can not be opened
     */
    void openConnection() throws SQLException;

    /**
     * Closes database connection
     *
     * @throws SQLException if connection can not be closed
     */
    void closeConnection() throws SQLException;

    /**
     * Send SQL update prepared statement. Symbol '?' is replaced by parameter
     *
     * @param command sql command string
     * @param params  parameters table
     * @throws SQLException if statement can not be send
     */
    void update(String command, String... params) throws SQLException;

    /**
     * Send SQL query prepared statement. Symbol '?' is replaced by parameter
     *
     * @param command sql command string
     * @param params  parameters table
     * @return result in ResultSet object
     * @throws SQLException if statement can not be send
     */
    ResultSet query(String command, String... params) throws SQLException;

}

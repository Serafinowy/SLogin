package me.serafin.slogin.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataBase {

    void openConnection() throws SQLException;

    void closeConnection() throws SQLException;

    void update(String command, String... params) throws SQLException;

    ResultSet query(String command, String... params) throws SQLException;

}

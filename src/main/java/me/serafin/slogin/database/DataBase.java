package me.serafin.slogin.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataBase {

    void openConnection() throws SQLException;

    void closeConnection() throws SQLException;

    void update(String command) throws SQLException;

    ResultSet query(String command) throws SQLException;

}

package com.jstnd.f1statsbot.database;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class Database {

    private Connection c = null;

    public Database(String url, String username, String password) {
        try {
            c = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getCircuitById(String circuitId) {
        String query = "SELECT * FROM circuits WHERE circuit_id=?";
        return getItemById(query, circuitId);
    }

    public ResultSet getCircuitList(int beginId, int endId) {
        String query = "SELECT circuit_id, name FROM circuits WHERE id >= ? AND id <= ? ORDER BY id";
        return getItemList(query, beginId, endId);
    }

    public String getCircuitThumbnail(String circuitId) {
        String query = "SELECT picture_url FROM circuits WHERE circuit_id=?";
        ResultSet rs = getItemById(query, circuitId);

        String thumbnail = "";
        try {
            while (rs.next()) {
                thumbnail = rs.getString("picture_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return thumbnail;
    }

    public ResultSet getConstructorById(String constructorId) {
        String query = "SELECT * FROM constructors WHERE constructor_id=?";
        return getItemById(query, constructorId);
    }

    public ResultSet getConstructorList(int beginId, int endId) {
        String query = "SELECT constructor_id, name FROM constructors WHERE id >= ? AND id <= ? ORDER BY id";
        return getItemList(query, beginId, endId);
    }

    public ResultSet getDriverById(String driverId) {
        String query = "SELECT * FROM drivers WHERE driver_id=?";
        return getItemById(query, driverId);
    }

    public ResultSet getDriverList(int beginId, int endId) {
        String query = "SELECT driver_id, name FROM drivers WHERE id >= ? AND id <= ? ORDER BY id";
        return getItemList(query, beginId, endId);
    }

    private ResultSet getItemById(String query, String itemId) {
        CachedRowSet crs = null;

        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setString(1, itemId);

            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return crs;
    }

    private ResultSet getItemList(String query, int beginId, int endId) {
        CachedRowSet crs = null;

        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setInt(1, beginId);
            stmt.setInt(2, endId);

            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return crs;
    }
}

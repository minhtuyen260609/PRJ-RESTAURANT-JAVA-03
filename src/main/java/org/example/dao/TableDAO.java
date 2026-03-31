package org.example.dao;

import org.example.model.RestaurantTable;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    public boolean insertTable(String name, int capacity, String status) {
        String sql = "insert into tables(name, capacity, status) values (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, capacity);
            statement.setString(3, status);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Loi them ban: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTable(int id, String name, int capacity) {
        String sql = "update tables set name = ?, capacity = ? where id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, capacity);
            statement.setInt(3, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Loi sua ban: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTable(int id) {
        String sql = "delete from tables where id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Loi xoa ban: " + e.getMessage());
            return false;
        }
    }

    public RestaurantTable findById(int id) {
        String sql = "select * from tables where id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                RestaurantTable table = new RestaurantTable();
                table.setId(resultSet.getInt("id"));
                table.setName(resultSet.getString("name"));
                table.setCapacity(resultSet.getInt("capacity"));
                table.setStatus(resultSet.getString("status"));
                return table;
            }
        } catch (Exception e) {
            System.out.println("Loi tim ban: " + e.getMessage());
        }

        return null;
    }

    public List<RestaurantTable> findAll() {
        List<RestaurantTable> tableList = new ArrayList<>();
        String sql = "select * from tables order by id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                RestaurantTable table = new RestaurantTable();
                table.setId(resultSet.getInt("id"));
                table.setName(resultSet.getString("name"));
                table.setCapacity(resultSet.getInt("capacity"));
                table.setStatus(resultSet.getString("status"));
                tableList.add(table);
            }
        } catch (Exception e) {
            System.out.println("Loi lay danh sach ban: " + e.getMessage());
        }

        return tableList;
    }

    public List<RestaurantTable> findFreeTables() {
        List<RestaurantTable> tableList = new ArrayList<>();
        String sql = "select * from tables where lower(status) = 'free' order by id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tableList.add(mapTable(resultSet));
            }
        } catch (Exception e) {
            System.out.println("Loi lay danh sach ban trong: " + e.getMessage());
        }

        return tableList;
    }

    public RestaurantTable findById(Connection connection, int id) throws Exception {
        String sql = "select * from tables where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapTable(resultSet);
                }
            }
        }

        return null;
    }

    public boolean occupyTable(Connection connection, int id) throws Exception {
        String sql = "update tables set status = 'occupied' where id = ? and lower(status) = 'free'";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean freeTable(Connection connection, int id) throws Exception {
        String sql = "update tables set status = 'free' where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private RestaurantTable mapTable(ResultSet resultSet) throws Exception {
        RestaurantTable table = new RestaurantTable();
        table.setId(resultSet.getInt("id"));
        table.setName(resultSet.getString("name"));
        table.setCapacity(resultSet.getInt("capacity"));
        table.setStatus(resultSet.getString("status"));
        return table;
    }
}

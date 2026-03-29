package org.example.dao;

import org.example.model.User;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public boolean insertUser(String username, String password, String role, boolean active) {
        String sql = "insert into users(username, password, role, is_active) values (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.setBoolean(4, active);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Loi them user: " + e.getMessage());
            return false;
        }
    }

    public User findByUsername(String username) {
        String sql = "select * from users where username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
                user.setActive(resultSet.getBoolean("is_active"));
                return user;
            }
        } catch (Exception e) {
            System.out.println("Loi tim user: " + e.getMessage());
        }

        return null;
    }
}

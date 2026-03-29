package org.example.dao;

import org.example.model.MenuItem;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    public boolean insertMenuItem(String name, double price, int quantity) {
        String sql = "insert into menu_items(name, price, quantity) values (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Loi them mon: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMenuItem(int id, String name, double price, int quantity) {
        String sql = "update menu_items set name = ?, price = ?, quantity = ? where id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.setInt(4, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Loi sua mon: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMenuItem(int id) {
        String sql = "delete from menu_items where id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Loi xoa mon: " + e.getMessage());
            return false;
        }
    }

    public MenuItem findById(int id) {
        String sql = "select * from menu_items where id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(resultSet.getInt("id"));
                menuItem.setName(resultSet.getString("name"));
                menuItem.setPrice(resultSet.getDouble("price"));
                menuItem.setQuantity(resultSet.getInt("quantity"));
                return menuItem;
            }
        } catch (Exception e) {
            System.out.println("Loi tim mon: " + e.getMessage());
        }

        return null;
    }

    public List<MenuItem> findByName(String keyword) {
        List<MenuItem> menuList = new ArrayList<>();
        String sql = "select * from menu_items where name like ? order by id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(resultSet.getInt("id"));
                menuItem.setName(resultSet.getString("name"));
                menuItem.setPrice(resultSet.getDouble("price"));
                menuItem.setQuantity(resultSet.getInt("quantity"));
                menuList.add(menuItem);
            }
        } catch (Exception e) {
            System.out.println("Loi tim kiem mon: " + e.getMessage());
        }

        return menuList;
    }

    public List<MenuItem> findAll() {
        List<MenuItem> menuList = new ArrayList<>();
        String sql = "select * from menu_items order by id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(resultSet.getInt("id"));
                menuItem.setName(resultSet.getString("name"));
                menuItem.setPrice(resultSet.getDouble("price"));
                menuItem.setQuantity(resultSet.getInt("quantity"));
                menuList.add(menuItem);
            }
        } catch (Exception e) {
            System.out.println("Loi lay danh sach mon: " + e.getMessage());
        }

        return menuList;
    }
}

package org.example.dao;

import org.example.model.Order;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public int insertOrder(Connection connection, int userId, int tableId, String status) throws Exception {
        String sql = "insert into orders(user_id, table_id, status) values (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setInt(2, tableId);
            statement.setString(3, status);
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        throw new IllegalStateException("Khong tao duoc don hang.");
    }

    public List<Order> findByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "select * from orders where user_id = ? order by id desc";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        } catch (Exception e) {
            System.out.println("Loi lay danh sach don hang: " + e.getMessage());
        }

        return orders;
    }

    private Order mapOrder(ResultSet resultSet) throws Exception {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setUserId(resultSet.getInt("user_id"));
        order.setTableId(resultSet.getInt("table_id"));
        order.setStatus(resultSet.getString("status"));
        return order;
    }

    public boolean closeOrder(Connection connection, int id) throws Exception {
        String sql = "update orders set status = 'closed' where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public Order findById(Connection connection, int id) throws Exception {
        String sql = "select * from orders where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
            }
        }

        return null;
    }
}

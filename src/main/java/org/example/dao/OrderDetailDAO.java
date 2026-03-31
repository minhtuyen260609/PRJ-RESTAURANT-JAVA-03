package org.example.dao;

import org.example.model.OrderDetail;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    public boolean insertOrderDetail(Connection connection, int orderId, int menuItemId, int quantity, String status) throws Exception {
        String sql = "insert into order_details(order_id, menu_item_id, quantity, status) values (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.setInt(2, menuItemId);
            statement.setInt(3, quantity);
            statement.setString(4, status);
            return statement.executeUpdate() > 0;
        }
    }

    public List<OrderDetail> findByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = """
                select od.id, od.order_id, od.menu_item_id, od.quantity, od.status, mi.name as menu_item_name
                from order_details od
                join menu_items mi on mi.id = od.menu_item_id
                where od.order_id = ?
                order by od.id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setId(resultSet.getInt("id"));
                    orderDetail.setOrderId(resultSet.getInt("order_id"));
                    orderDetail.setMenuItemId(resultSet.getInt("menu_item_id"));
                    orderDetail.setQuantity(resultSet.getInt("quantity"));
                    orderDetail.setMenuItemName(resultSet.getString("menu_item_name"));
                    orderDetail.setStatus(resultSet.getString("status"));
                    orderDetails.add(orderDetail);
                }
            }
        } catch (Exception e) {
            System.out.println("Loi lay chi tiet don hang: " + e.getMessage());
        }

        return orderDetails;
    }

    public List<OrderDetail> findByCustomerId(int customerId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = """
                select od.id, od.order_id, od.menu_item_id, od.quantity, od.status, mi.name as menu_item_name
                from order_details od
                join orders o on o.id = od.order_id
                join menu_items mi on mi.id = od.menu_item_id
                where o.user_id = ?
                order by od.id desc
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setId(resultSet.getInt("id"));
                    orderDetail.setOrderId(resultSet.getInt("order_id"));
                    orderDetail.setMenuItemId(resultSet.getInt("menu_item_id"));
                    orderDetail.setQuantity(resultSet.getInt("quantity"));
                    orderDetail.setMenuItemName(resultSet.getString("menu_item_name"));
                    orderDetail.setStatus(resultSet.getString("status"));
                    orderDetails.add(orderDetail);
                }
            }
        } catch (Exception e) {
            System.out.println("Loi lay mon da goi: " + e.getMessage());
        }

        return orderDetails;
    }

    public List<OrderDetail> findByStatus(String status) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = """
                select od.id, od.order_id, od.menu_item_id, od.quantity, od.status, mi.name as menu_item_name
                from order_details od
                join menu_items mi on mi.id = od.menu_item_id
                where lower(od.status) = lower(?)
                order by od.id asc
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setId(resultSet.getInt("id"));
                    orderDetail.setOrderId(resultSet.getInt("order_id"));
                    orderDetail.setMenuItemId(resultSet.getInt("menu_item_id"));
                    orderDetail.setQuantity(resultSet.getInt("quantity"));
                    orderDetail.setMenuItemName(resultSet.getString("menu_item_name"));
                    orderDetail.setStatus(resultSet.getString("status"));
                    orderDetails.add(orderDetail);
                }
            }
        } catch (Exception e) {
            System.out.println("Loi lay mon theo trang thai: " + e.getMessage());
        }

        return orderDetails;
    }

    public List<OrderDetail> findKitchenItems() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = """
                select od.id, od.order_id, od.menu_item_id, od.quantity, od.status, mi.name as menu_item_name
                from order_details od
                join menu_items mi on mi.id = od.menu_item_id
                where lower(od.status) <> 'served'
                order by od.id asc
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(resultSet.getInt("id"));
                orderDetail.setOrderId(resultSet.getInt("order_id"));
                orderDetail.setMenuItemId(resultSet.getInt("menu_item_id"));
                orderDetail.setQuantity(resultSet.getInt("quantity"));
                orderDetail.setMenuItemName(resultSet.getString("menu_item_name"));
                orderDetail.setStatus(resultSet.getString("status"));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            System.out.println("Loi lay danh sach bep: " + e.getMessage());
        }

        return orderDetails;
    }

    public OrderDetail findById(int id) {
        String sql = """
                select od.id, od.order_id, od.menu_item_id, od.quantity, od.status, mi.name as menu_item_name
                from order_details od
                join menu_items mi on mi.id = od.menu_item_id
                where od.id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setId(resultSet.getInt("id"));
                    orderDetail.setOrderId(resultSet.getInt("order_id"));
                    orderDetail.setMenuItemId(resultSet.getInt("menu_item_id"));
                    orderDetail.setQuantity(resultSet.getInt("quantity"));
                    orderDetail.setMenuItemName(resultSet.getString("menu_item_name"));
                    orderDetail.setStatus(resultSet.getString("status"));
                    return orderDetail;
                }
            }
        } catch (Exception e) {
            System.out.println("Loi tim mon da goi: " + e.getMessage());
        }

        return null;
    }

    public boolean belongsToCustomer(int orderDetailId, int customerId) {
        String sql = """
                select od.id
                from order_details od
                join orders o on o.id = od.order_id
                where od.id = ? and o.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderDetailId);
            statement.setInt(2, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (Exception e) {
            System.out.println("Loi kiem tra mon da goi: " + e.getMessage());
        }

        return false;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "update order_details set status = ? where id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Loi cap nhat trang thai mon: " + e.getMessage());
        }

        return false;
    }

    public boolean updateStatus(Connection connection, int id, String status) throws Exception {
        String sql = "update order_details set status = ? where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(Connection connection, int id) throws Exception {
        String sql = "delete from order_details where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean hasItemsInOrder(Connection connection, int orderId) throws Exception {
        String sql = "select id from order_details where order_id = ? limit 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}

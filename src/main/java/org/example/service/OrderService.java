package org.example.service;

import org.example.dao.OrderDAO;
import org.example.dao.OrderDetailDAO;
import org.example.dao.TableDAO;
import org.example.model.Order;
import org.example.model.OrderDetail;
import org.example.model.RestaurantTable;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final TableDAO tableDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderDetailDAO = new OrderDetailDAO();
        this.tableDAO = new TableDAO();
    }

    public Order placeOrder(int userId, int tableId, Map<Integer, Integer> selectedItems) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Khach hang khong hop le.");
        }
        if (tableId <= 0) {
            throw new IllegalArgumentException("Ban khong hop le.");
        }
        if (selectedItems == null || selectedItems.isEmpty()) {
            throw new IllegalArgumentException("Don hang phai co it nhat 1 mon.");
        }

        try (Connection connection = DBConnection.getConnection()) {
            RestaurantTable table = tableDAO.findById(connection, tableId);
            if (table == null) {
                throw new IllegalStateException("Khong tim thay ban.");
            }
            if (!isAvailableStatus(table.getStatus())) {
                throw new IllegalStateException("Ban nay da co khach. Vui long chon ban khac.");
            }

            for (Map.Entry<Integer, Integer> entry : selectedItems.entrySet()) {
                if (entry.getValue() <= 0) {
                    throw new IllegalArgumentException("So luong mon phai lon hon 0.");
                }
            }

            boolean occupied = tableDAO.occupyTable(connection, tableId);
            if (!occupied) {
                throw new IllegalStateException("Khong giu duoc ban. Vui long thu lai.");
            }

            int orderId = orderDAO.insertOrder(connection, userId, tableId, "open");

            for (Map.Entry<Integer, Integer> entry : selectedItems.entrySet()) {
                boolean inserted = orderDetailDAO.insertOrderDetail(
                        connection,
                        orderId,
                        entry.getKey(),
                        entry.getValue(),
                        "pending"
                );

                if (!inserted) {
                    throw new IllegalStateException("Khong luu duoc chi tiet don hang.");
                }
            }

            Order order = new Order();
            order.setId(orderId);
            order.setUserId(userId);
            order.setTableId(tableId);
            order.setStatus("open");
            return order;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Loi xu ly don hang: " + e.getMessage(), e);
        }
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return orderDAO.findByUserId(customerId);
    }

    public List<OrderDetail> getOrderDetails(int orderId) {
        return orderDetailDAO.findByOrderId(orderId);
    }

    public List<OrderDetail> getOrderedItemsByCustomer(int customerId) {
        return orderDetailDAO.findByCustomerId(customerId);
    }

    public List<OrderDetail> getKitchenItems() {
        return orderDetailDAO.findKitchenItems();
    }

    public boolean advanceOrderDetailStatus(int orderDetailId) {
        if (orderDetailId <= 0) {
            throw new IllegalArgumentException("Id mon khong hop le.");
        }

        OrderDetail orderDetail = orderDetailDAO.findById(orderDetailId);
        if (orderDetail == null) {
            return false;
        }

        String nextStatus = getNextStatus(orderDetail.getStatus());
        if (nextStatus == null) {
            throw new IllegalStateException("Mon nay da hoan tat, khong can cap nhat them.");
        }

        return orderDetailDAO.updateStatus(orderDetailId, nextStatus);
    }

    public boolean cancelOrderDetail(int customerId, int orderDetailId) {
        OrderDetail orderDetail = orderDetailDAO.findById(orderDetailId);
        if (orderDetail == null) {
            return false;
        }
        if (!"pending".equalsIgnoreCase(orderDetail.getStatus())) {
            throw new IllegalStateException("Mon nay khong the huy vi da chuyen sang trang thai khac.");
        }

        try (Connection connection = DBConnection.getConnection()) {
            boolean deleted = orderDetailDAO.deleteById(connection, orderDetailId);
            if (!deleted) {
                throw new IllegalStateException("Khong huy duoc mon.");
            }

            if (!orderDetailDAO.hasItemsInOrder(connection, orderDetail.getOrderId())) {
                Order order = orderDAO.findById(connection, orderDetail.getOrderId());
                if (order == null) {
                    throw new IllegalStateException("Khong tim thay don hang.");
                }
                orderDAO.closeOrder(connection, orderDetail.getOrderId());
                tableDAO.freeTable(connection, order.getTableId());
            }

            return true;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Loi huy mon: " + e.getMessage(), e);
        }
    }

    private boolean isAvailableStatus(String status) {
        return "free".equalsIgnoreCase(status);
    }

    private String getNextStatus(String currentStatus) {
        if ("pending".equalsIgnoreCase(currentStatus)) {
            return "cooking";
        }
        if ("cooking".equalsIgnoreCase(currentStatus)) {
            return "ready";
        }
        if ("ready".equalsIgnoreCase(currentStatus)) {
            return "served";
        }
        return null;
    }
}

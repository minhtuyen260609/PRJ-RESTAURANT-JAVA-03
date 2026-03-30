package org.example.service;

import org.example.dao.MenuItemDAO;
import org.example.dao.OrderDAO;
import org.example.dao.OrderDetailDAO;
import org.example.dao.TableDAO;
import org.example.model.MenuItem;
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
    private final MenuItemDAO menuItemDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderDetailDAO = new OrderDetailDAO();
        this.tableDAO = new TableDAO();
        this.menuItemDAO = new MenuItemDAO();
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
            connection.setAutoCommit(false);

            try {
                RestaurantTable table = tableDAO.findById(connection, tableId);
                if (table == null) {
                    throw new IllegalStateException("Khong tim thay ban.");
                }
                if (!isAvailableStatus(table.getStatus())) {
                    throw new IllegalStateException("Ban nay da co khach. Vui long chon ban khac.");
                }

                boolean occupied = tableDAO.occupyTable(connection, tableId);
                if (!occupied) {
                    throw new IllegalStateException("Khong giu duoc ban. Vui long thu lai.");
                }

                for (Map.Entry<Integer, Integer> entry : selectedItems.entrySet()) {
                    int menuItemId = entry.getKey();
                    int quantity = entry.getValue();

                    if (quantity <= 0) {
                        throw new IllegalArgumentException("So luong mon phai lon hon 0.");
                    }

                    MenuItem menuItem = menuItemDAO.findById(connection, menuItemId);
                    if (menuItem == null) {
                        throw new IllegalStateException("Khong tim thay mon co id = " + menuItemId);
                    }
                    if (menuItem.getQuantity() < quantity) {
                        throw new IllegalStateException("Mon " + menuItem.getName() + " khong du so luong.");
                    }

                    boolean updated = menuItemDAO.updateQuantity(connection, menuItemId, menuItem.getQuantity() - quantity);
                    if (!updated) {
                        throw new IllegalStateException("Khong cap nhat duoc so luong mon.");
                    }
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

                connection.commit();

                Order order = new Order();
                order.setId(orderId);
                order.setUserId(userId);
                order.setTableId(tableId);
                order.setStatus("open");
                return order;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
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

    public boolean cancelOrderDetail(int customerId, int orderDetailId) {
        if (!orderDetailDAO.belongsToCustomer(orderDetailId, customerId)) {
            return false;
        }

        OrderDetail orderDetail = orderDetailDAO.findById(orderDetailId);
        if (orderDetail == null) {
            return false;
        }
        if (!"pending".equalsIgnoreCase(orderDetail.getStatus())) {
            throw new IllegalStateException("Mon nay khong the huy vi da chuyen sang trang thai khac.");
        }

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                MenuItem menuItem = menuItemDAO.findById(connection, orderDetail.getMenuItemId());
                if (menuItem == null) {
                    throw new IllegalStateException("Khong tim thay mon de hoan lai ton kho.");
                }

                boolean restored = menuItemDAO.updateQuantity(
                        connection,
                        orderDetail.getMenuItemId(),
                        menuItem.getQuantity() + orderDetail.getQuantity()
                );
                if (!restored) {
                    throw new IllegalStateException("Khong hoan lai duoc ton kho.");
                }

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

                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Loi huy mon: " + e.getMessage(), e);
        }
    }

    private boolean isAvailableStatus(String status) {
        return "empty".equalsIgnoreCase(status);
    }
}

package org.example.presentation;

import org.example.model.MenuItem;
import org.example.model.Order;
import org.example.model.OrderDetail;
import org.example.model.RestaurantTable;
import org.example.model.User;
import org.example.service.MenuService;
import org.example.service.OrderService;
import org.example.service.TableService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CustomerPresentation {
    private final Scanner scanner;
    private final MenuService menuService;
    private final TableService tableService;
    private final OrderService orderService;

    public CustomerPresentation(Scanner scanner, MenuService menuService, TableService tableService, OrderService orderService) {
        this.scanner = scanner;
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    public void showMenu(User user) {
        while (true) {
            System.out.println("\n===== MENU KHACH HANG =====");
            System.out.println("Xin chao " + user.getUsername());
            System.out.println("1. Xem thuc don");
            System.out.println("2. Xem danh sach ban");
            System.out.println("3. Chon ban va goi mon");
            System.out.println("4. Theo doi mon da goi");
            System.out.println("5. Huy goi do");
            System.out.println("0. Dang xuat");

            int choice = InputPresentation.readInt(scanner, "Nhap lua chon: ");

            switch (choice) {
                case 1:
                    showMenuList();
                    break;
                case 2:
                    showTableList();
                    break;
                case 3:
                    showOrderScreen(user);
                    break;
                case 4:
                    showOrderedItems(user);
                    break;
                case 5:
                    cancelOrderedItem(user);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }

    private void showMenuList() {
        List<MenuItem> menuItems = menuService.getAllMenuItems();
        System.out.println("\n----- DANH SACH MON -----");
        System.out.printf("%-5s %-25s %-12s %-10s%n", "ID", "Ten mon", "Gia", "So luong");

        if (menuItems.isEmpty()) {
            System.out.println("Chua co mon nao.");
            return;
        }

        for (MenuItem item : menuItems) {
            System.out.printf("%-5d %-25s %-12.0f %-10d%n",
                    item.getId(), item.getName(), item.getPrice(), item.getQuantity());
        }
    }

    private void showTableList() {
        List<RestaurantTable> tables = tableService.getAllTables();
        System.out.println("\n----- DANH SACH BAN -----");
        System.out.printf("%-5s %-20s %-12s %-12s%n", "ID", "Ten ban", "Suc chua", "Trang thai");

        if (tables.isEmpty()) {
            System.out.println("Chua co ban nao.");
            return;
        }

        for (RestaurantTable table : tables) {
            System.out.printf("%-5d %-20s %-12d %-12s%n",
                    table.getId(), table.getName(), table.getCapacity(), table.getStatus());
        }
    }

    private void showOrderScreen(User user) {
        System.out.println("\n----- CHON BAN VA GOI MON -----");
        List<RestaurantTable> freeTables = tableService.getFreeTables();
        if (freeTables.isEmpty()) {
            System.out.println("Hien tai khong con ban trong.");
            return;
        }

        System.out.println("Danh sach ban trong:");
        printTables(freeTables);
        int tableId = InputPresentation.readInt(scanner, "Nhap id ban muon chon: ");

        RestaurantTable selectedTable = tableService.findById(tableId);
        if (selectedTable == null) {
            System.out.println("Khong tim thay ban.");
            return;
        }
        if (!isAvailableStatus(selectedTable.getStatus())) {
            System.out.println("Ban nay da co khach. Vui long chon ban khac.");
            return;
        }

        List<MenuItem> menuItems = menuService.getAllMenuItems();
        if (menuItems.isEmpty()) {
            System.out.println("Chua co mon nao de goi.");
            return;
        }

        Map<Integer, Integer> selectedItems = new LinkedHashMap<>();
        while (true) {
            showMenuList();
            int menuItemId = InputPresentation.readInt(scanner, "Nhap id mon muon goi (0 de ket thuc): ");
            if (menuItemId == 0) {
                break;
            }

            MenuItem menuItem = menuService.findById(menuItemId);
            if (menuItem == null) {
                System.out.println("Khong tim thay mon.");
                continue;
            }

            int quantity = InputPresentation.readInt(scanner, "Nhap so luong: ");
            if (quantity <= 0) {
                System.out.println("So luong phai lon hon 0.");
                continue;
            }

            selectedItems.put(menuItemId, selectedItems.getOrDefault(menuItemId, 0) + quantity);
            System.out.println("Da them " + menuItem.getName() + " x" + quantity);
        }

        if (selectedItems.isEmpty()) {
            System.out.println("Ban chua chon mon nao.");
            return;
        }

        try {
            Order order = orderService.placeOrder(user.getId(), tableId, selectedItems);
            System.out.println("Goi mon thanh cong.");
            System.out.println("Ma don: " + order.getId());
            System.out.println("Ban: " + selectedTable.getName() + " -> occupied");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showOrderedItems(User user) {
        List<OrderDetail> orderDetails = orderService.getOrderedItemsByCustomer(user.getId());
        if (orderDetails.isEmpty()) {
            System.out.println("Ban chua goi mon nao.");
            return;
        }

        System.out.println("\n----- MON DA GOI -----");
        System.out.printf("%-5s %-8s %-25s %-10s %-12s%n",
                "ID", "Order", "Ten mon", "So luong", "Trang thai");
        for (OrderDetail orderDetail : orderDetails) {
            System.out.printf("%-5d %-8d %-25s %-10d %-12s%n",
                    orderDetail.getId(),
                    orderDetail.getOrderId(),
                    orderDetail.getMenuItemName(),
                    orderDetail.getQuantity(),
                    orderDetail.getStatus());
        }
    }

    private void printTables(List<RestaurantTable> tables) {
        System.out.printf("%-5s %-20s %-12s %-12s%n", "ID", "Ten ban", "Suc chua", "Trang thai");
        for (RestaurantTable table : tables) {
            System.out.printf("%-5d %-20s %-12d %-12s%n",
                    table.getId(), table.getName(), table.getCapacity(), table.getStatus());
        }
    }

    private boolean isAvailableStatus(String status) {
        return "free".equalsIgnoreCase(status);
    }

    private void cancelOrderedItem(User user) {
        List<OrderDetail> orderDetails = orderService.getOrderedItemsByCustomer(user.getId());
        if (orderDetails.isEmpty()) {
            System.out.println("Ban chua goi mon nao.");
            return;
        }

        showOrderedItems(user);
        int orderDetailId = InputPresentation.readInt(scanner, "Nhap id mon muon huy: ");

        try {
            boolean result = orderService.cancelOrderDetail(user.getId(), orderDetailId);
            if (result) {
                System.out.println("Huy mon thanh cong.");
            } else {
                System.out.println("Khong tim thay mon can huy.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

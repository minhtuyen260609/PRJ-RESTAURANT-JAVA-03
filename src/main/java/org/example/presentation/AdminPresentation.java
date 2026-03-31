package org.example.presentation;

import org.example.model.MenuItem;
import org.example.model.RestaurantTable;
import org.example.model.User;
import org.example.service.MenuService;
import org.example.service.TableService;

import java.util.List;
import java.util.Scanner;

public class AdminPresentation {
    private final Scanner scanner;
    private final MenuService menuService;
    private final TableService tableService;

    public AdminPresentation(Scanner scanner, MenuService menuService, TableService tableService) {
        this.scanner = scanner;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    public void showMenu(User user) {
        while (true) {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("Xin chao " + user.getUsername());
            System.out.println("1. Xem danh sach mon");
            System.out.println("2. Them mon moi");
            System.out.println("3. Sua mon");
            System.out.println("4. Xoa mon");
            System.out.println("5. Tim kiem mon theo ten");
            System.out.println("6. Xem danh sach ban");
            System.out.println("7. Them ban moi");
            System.out.println("8. Sua ban");
            System.out.println("9. Xoa ban");
            System.out.println("0. Dang xuat");

            int choice = InputPresentation.readInt(scanner, "Nhap lua chon: ");

            switch (choice) {
                case 1:
                    showMenuList();
                    break;
                case 2:
                    addMenuItem();
                    break;
                case 3:
                    updateMenuItem();
                    break;
                case 4:
                    deleteMenuItem();
                    break;
                case 5:
                    searchMenuItem();
                    break;
                case 6:
                    showTableList();
                    break;
                case 7:
                    addTable();
                    break;
                case 8:
                    updateTable();
                    break;
                case 9:
                    deleteTable();
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

    private void addMenuItem() {
        System.out.println("\n----- THEM MON -----");
        String name = readMenuItemName("Nhap ten mon: ");
        double price = readMenuItemPrice("Nhap gia: ");
        int quantity = readMenuItemQuantity("Nhap so luong: ");

        boolean result = menuService.addMenuItem(name, price, quantity);
        if (result) {
            System.out.println("Them mon thanh cong.");
        } else {
            System.out.println("Them mon that bai. Kiem tra ten, gia va so luong.");
        }
    }

    private void updateMenuItem() {
        System.out.println("\n----- SUA MON -----");
        showMenuList();
        int id = InputPresentation.readInt(scanner, "Nhap id mon can sua: ");

        MenuItem oldItem = menuService.findById(id);
        if (oldItem == null) {
            System.out.println("Khong tim thay mon.");
            return;
        }

        System.out.println("Thong tin hien tai: " + oldItem.getName() + " - " + oldItem.getPrice() + " - " + oldItem.getQuantity());
        String name = readMenuItemName("Nhap ten mon moi: ");
        double price = readMenuItemPrice("Nhap gia moi: ");
        int quantity = readMenuItemQuantity("Nhap so luong moi: ");

        boolean result = menuService.updateMenuItem(id, name, price, quantity);
        if (result) {
            System.out.println("Sua mon thanh cong.");
        } else {
            System.out.println("Sua mon that bai.");
        }
    }

    private void deleteMenuItem() {
        System.out.println("\n----- XOA MON -----");
        showMenuList();
        int id = InputPresentation.readInt(scanner, "Nhap id mon can xoa: ");

        MenuItem oldItem = menuService.findById(id);
        if (oldItem == null) {
            System.out.println("Khong tim thay mon.");
            return;
        }

        String confirm = InputPresentation.readLine(scanner, "Ban co chac muon xoa? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Da huy xoa.");
            return;
        }

        boolean result = menuService.deleteMenuItem(id);
        if (result) {
            System.out.println("Xoa mon thanh cong.");
        } else {
            System.out.println("Xoa mon that bai.");
        }
    }

    private void searchMenuItem() {
        System.out.println("\n----- TIM KIEM MON -----");
        String keyword = InputPresentation.readLine(scanner, "Nhap ten mon muon tim: ");
        List<MenuItem> menuItems = menuService.searchByName(keyword);

        if (menuItems.isEmpty()) {
            System.out.println("Khong tim thay mon phu hop.");
            return;
        }

        System.out.printf("%-5s %-25s %-12s %-10s%n", "ID", "Ten mon", "Gia", "So luong");
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

    private void addTable() {
        System.out.println("\n----- THEM BAN -----");
        String name = readTableName("Nhap ten ban: ");
        int capacity = readTableCapacity("Nhap suc chua: ");

        boolean result = tableService.addTable(name, capacity);
        if (result) {
            System.out.println("Them ban thanh cong.");
        } else {
            System.out.println("Them ban that bai. Kiem tra ten va suc chua.");
        }
    }

    private void updateTable() {
        System.out.println("\n----- SUA BAN -----");
        showTableList();
        int id = InputPresentation.readInt(scanner, "Nhap id ban can sua: ");

        RestaurantTable oldTable = tableService.findById(id);
        if (oldTable == null) {
            System.out.println("Khong tim thay ban.");
            return;
        }

        System.out.println("Thong tin hien tai: " + oldTable.getName() + " - " + oldTable.getCapacity());
        String name = readTableName("Nhap ten ban moi: ");
        int capacity = readTableCapacity("Nhap suc chua moi: ");

        boolean result = tableService.updateTable(id, name, capacity);
        if (result) {
            System.out.println("Sua ban thanh cong.");
        } else {
            System.out.println("Sua ban that bai.");
        }
    }

    private void deleteTable() {
        System.out.println("\n----- XOA BAN -----");
        showTableList();
        int id = InputPresentation.readInt(scanner, "Nhap id ban can xoa: ");

        RestaurantTable oldTable = tableService.findById(id);
        if (oldTable == null) {
            System.out.println("Khong tim thay ban.");
            return;
        }

        String confirm = InputPresentation.readLine(scanner, "Ban co chac muon xoa? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Da huy xoa.");
            return;
        }

        boolean result = tableService.deleteTable(id);
        if (result) {
            System.out.println("Xoa ban thanh cong.");
        } else {
            System.out.println("Xoa ban that bai.");
        }
    }

    private String readMenuItemName(String message) {
        while (true) {
            System.out.print(message);
            String name = scanner.nextLine();
            if (name != null && !name.trim().isEmpty()) {
                return name;
            }
            System.out.println("Chua nhap ten mon.");
        }
    }

    private double readMenuItemPrice(String message) {
        while (true) {
            double price = InputPresentation.readDouble(scanner, message);
            if (price > 0) {
                return price;
            }
            System.out.println("Gia phai lon hon 0.");
        }
    }

    private int readMenuItemQuantity(String message) {
        while (true) {
            int quantity = InputPresentation.readInt(scanner, message);
            if (quantity > 0) {
                return quantity;
            }
            System.out.println("So luong phai lon hon 0.");
        }
    }

    private String readTableName(String message) {
        while (true) {
            System.out.print(message);
            String name = scanner.nextLine();
            if (name != null && !name.trim().isEmpty()) {
                return name;
            }
            System.out.println("Chua nhap ten ban.");
        }
    }

    private int readTableCapacity(String message) {
        while (true) {
            int capacity = InputPresentation.readInt(scanner, message);
            if (capacity > 0) {
                return capacity;
            }
            System.out.println("So nguoi phai lon hon 0.");
        }
    }
}

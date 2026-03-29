package org.example.presentation;

import org.example.model.MenuItem;
import org.example.model.RestaurantTable;
import org.example.model.User;
import org.example.service.MenuService;
import org.example.service.TableService;

import java.util.List;
import java.util.Scanner;

public class CustomerPresentation {
    private final Scanner scanner;
    private final MenuService menuService;
    private final TableService tableService;

    public CustomerPresentation(Scanner scanner, MenuService menuService, TableService tableService) {
        this.scanner = scanner;
        this.menuService = menuService;
        this.tableService = tableService;
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
                    showOrderScreen();
                    break;
                case 4:
                    System.out.println("Giao dien theo doi mon da goi da san sang. Chua noi logic.");
                    break;
                case 5:
                    System.out.println("Giao dien huy goi do da san sang. Chua noi logic.");
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

    private void showOrderScreen() {
        System.out.println("\n----- CHON BAN VA GOI MON -----");
        showTableList();
        InputPresentation.readInt(scanner, "Nhap id ban muon chon: ");
        showMenuList();
        InputPresentation.readInt(scanner, "Nhap id mon muon goi: ");
        InputPresentation.readInt(scanner, "Nhap so luong: ");
        System.out.println("Giao dien goi mon da san sang. Chua noi logic.");
    }
}

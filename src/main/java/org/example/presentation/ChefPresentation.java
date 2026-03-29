package org.example.presentation;

import org.example.model.MenuItem;
import org.example.model.User;
import org.example.service.MenuService;

import java.util.List;
import java.util.Scanner;

public class ChefPresentation {
    private final Scanner scanner;
    private final MenuService menuService;

    public ChefPresentation(Scanner scanner, MenuService menuService) {
        this.scanner = scanner;
        this.menuService = menuService;
    }

    public void showMenu(User user) {
        while (true) {
            System.out.println("\n===== MENU CHEF =====");
            System.out.println("Xin chao " + user.getUsername());
            System.out.println("1. Xem danh sach mon hien co");
            System.out.println("2. Hien thi mon dang cho nau");
            System.out.println("3. Cap nhat trang thai mon");
            System.out.println("0. Dang xuat");

            int choice = InputPresentation.readInt(scanner, "Nhap lua chon: ");

            switch (choice) {
                case 1:
                    showMenuList();
                    break;
                case 2:

                    break;
                case 3:
                    showUpdateStatusScreen();
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

    private void showUpdateStatusScreen() {
        System.out.println("\n----- CAP NHAT TRANG THAI MON -----");
        System.out.println("1. PENDING -> COOKING");
        System.out.println("2. COOKING -> READY");
        System.out.println("3. READY -> SERVED");
        System.out.println("0. Quay lai");
        InputPresentation.readInt(scanner, "Nhap lua chon: ");
        System.out.println("Giao dien cap nhat trang thai da san sang. Chua noi logic.");
    }
}

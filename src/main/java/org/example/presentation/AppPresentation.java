package org.example.presentation;

import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.MenuService;
import org.example.service.TableService;

import java.util.Scanner;

public class AppPresentation {
    private final Scanner scanner;
    private final AuthPresentation authPresentation;
    private final AdminPresentation adminPresentation;
    private final ChefPresentation chefPresentation;
    private final CustomerPresentation customerPresentation;

    public AppPresentation(AuthService authService, MenuService menuService, TableService tableService) {
        this.scanner = new Scanner(System.in);
        this.authPresentation = new AuthPresentation(scanner, authService);
        this.adminPresentation = new AdminPresentation(scanner, menuService, tableService);
        this.chefPresentation = new ChefPresentation(scanner, menuService);
        this.customerPresentation = new CustomerPresentation(scanner, menuService, tableService);
    }

    public void start() {
        while (true) {
            showStartMenu();
            int choice = InputPresentation.readInt(scanner, "Nhap lua chon: ");

            switch (choice) {
                case 1:
                    authPresentation.registerCustomer();
                    break;
                case 2:
                    User user = authPresentation.login();
                    if (user != null) {
                        openMenuByRole(user);
                    }
                    break;
                case 0:
                    System.out.println("Thoat chuong trinh.");
                    return;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }

    private void showStartMenu() {
        System.out.println("\n===== QUAN LY NHA HANG =====");
        System.out.println("1. Dang ky tai khoan khach hang");
        System.out.println("2. Dang nhap");
        System.out.println("0. Thoat");
    }

    private void openMenuByRole(User user) {
        switch (user.getRole().toLowerCase()) {
            case "manager":
                adminPresentation.showMenu(user);
                break;
            case "chef":
                chefPresentation.showMenu(user);
                break;
            case "customer":
                customerPresentation.showMenu(user);
                break;
            default:
                System.out.println("Role khong hop le.");
        }
    }
}

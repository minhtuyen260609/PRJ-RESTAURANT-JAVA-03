package org.example.presentation;

import org.example.model.User;
import org.example.service.AuthService;

import java.util.Scanner;

public class AuthPresentation {
    private final Scanner scanner;
    private final AuthService authService;

    public AuthPresentation(Scanner scanner, AuthService authService) {
        this.scanner = scanner;
        this.authService = authService;
    }

    public void registerCustomer() {
        System.out.println("\n----- DANG KY KHACH HANG -----");
        System.out.print("Nhap username: ");
        String username = scanner.nextLine();
        System.out.print("Nhap password: ");
        String password = scanner.nextLine();

        boolean result = authService.registerCustomer(username, password);
        if (result) {
            System.out.println("Dang ky thanh cong.");
        } else {
            System.out.println("Dang ky that bai. Username co the da ton tai hoac du lieu rong.");
        }
    }

    public User login() {
        System.out.println("\n----- DANG NHAP -----");
        System.out.print("Nhap username: ");
        String username = scanner.nextLine();
        System.out.print("Nhap password: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);
        if (user == null) {
            System.out.println("Sai tai khoan hoac mat khau.");
            return null;
        }

        System.out.println("Dang nhap thanh cong voi vai tro: " + user.getRole());
        return user;
    }
}

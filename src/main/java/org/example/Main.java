package org.example;

import org.example.service.AuthService;
import org.example.service.MenuService;
import org.example.service.OrderService;
import org.example.service.TableService;
import org.example.presentation.AppPresentation;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        MenuService menuService = new MenuService();
        TableService tableService = new TableService();
        OrderService orderService = new OrderService();

        AppPresentation appPresentation = new AppPresentation(authService, menuService, tableService, orderService);
        appPresentation.start();
    }
}

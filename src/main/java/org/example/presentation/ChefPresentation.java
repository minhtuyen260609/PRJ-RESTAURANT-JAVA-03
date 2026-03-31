package org.example.presentation;

import org.example.model.OrderDetail;
import org.example.model.User;
import org.example.service.OrderService;

import java.util.List;
import java.util.Scanner;

public class ChefPresentation {
    private final Scanner scanner;
    private final OrderService orderService;

    public ChefPresentation(Scanner scanner, OrderService orderService) {
        this.scanner = scanner;
        this.orderService = orderService;
    }

    public void showMenu(User user) {
        while (true) {
            System.out.println("\n===== MENU CHEF =====");
            System.out.println("Xin chao " + user.getUsername());
            System.out.println("1. Xem danh sach mon can xu ly");
            System.out.println("2. Cap nhat trang thai mon");
            System.out.println("0. Dang xuat");

            int choice = InputPresentation.readInt(scanner, "Nhap lua chon: ");

            switch (choice) {
                case 1:
                    showKitchenItems();
                    break;
                case 2:
                    showUpdateStatusScreen();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }

    private void showKitchenItems() {
        List<OrderDetail> orderDetails = orderService.getKitchenItems();
        System.out.println("\n----- DANH SACH MON CAN XU LY -----");
        printOrderDetails(orderDetails);
    }

    private void showUpdateStatusScreen() {
        System.out.println("\n----- CAP NHAT TRANG THAI MON -----");
        List<OrderDetail> orderDetails = orderService.getKitchenItems();
        if (orderDetails.isEmpty()) {
            System.out.println("Khong co mon nao de cap nhat.");
            return;
        }

        printOrderDetails(orderDetails);
        int orderDetailId = InputPresentation.readInt(scanner, "Nhap id mon can cap nhat: ");

        try {
            boolean result = orderService.advanceOrderDetailStatus(orderDetailId);
            if (result) {
                System.out.println("Cap nhat trang thai thanh cong.");
            } else {
                System.out.println("Khong tim thay mon can cap nhat.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printOrderDetails(List<OrderDetail> orderDetails) {
        if (orderDetails.isEmpty()) {
            System.out.println("Khong co mon nao.");
            return;
        }

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
}

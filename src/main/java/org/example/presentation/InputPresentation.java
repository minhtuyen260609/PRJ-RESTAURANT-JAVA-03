package org.example.presentation;

import java.util.Scanner;

public class InputPresentation {
    public static String readLine(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static int readInt(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Vui long nhap so nguyen.");
            }
        }
    }

    public static double readDouble(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Vui long nhap so hop le.");
            }
        }
    }
}

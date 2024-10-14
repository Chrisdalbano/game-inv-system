package utils;
import java.io.*;
import java.util.*;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);

    public static int getValidInt(String prompt) {
        int value = -1;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print(prompt);
                value = Integer.parseInt(scanner.nextLine().trim());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return value;
    }

    public static double getValidDouble(String prompt) {
        double value = -1.0;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print(prompt);
                value = Double.parseDouble(scanner.nextLine().trim());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return value;
    }
}
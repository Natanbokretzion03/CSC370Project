/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parser;
import java.util.Scanner;




public class Parser {
     private static Scanner scanner;
    private static String input;

    public static void main(String[] args) {
       scanner = new Scanner(System.in);
        System.out.print("Enter an expression: ");
        input = scanner.nextLine();
        int result = parseE();
        System.out.println("Result: " + result);

    
    }
     private static int parseE() {
        int r = parseTerm();
        while (input.matches("^[+-].*")) {
            char operator = input.charAt(0);
            input = input.substring(1).trim();
            int term = parseTerm();
            if (operator == '+') {
                r += term;
            } else {
                r -= term;
            }
        }
        return r;
    }

    private static int parseTerm() {
        int result = parseF();
        while (input.matches("^[*/].*")) {
            char operator = input.charAt(0);
            input = input.substring(1).trim();
            int fac = parseF();
            if (operator == '*') {
                result *= fac;
            } else {
                result /= fac;
            }
        }
        return result;
    }

    private static int parseF() {
        int r;
        char firstC = input.charAt(0);
        if (Character.isDigit(firstC)) {
            int endIndex = input.length();
            for (int i = 1; i < input.length(); i++) {
                char c = input.charAt(i);
                if (!Character.isDigit(c)) {
                    endIndex = i;
                    break;
                }
            }
            r = Integer.parseInt(input.substring(0, endIndex));
            input = input.substring(endIndex).trim();
        } else if (firstC == '(') {
            input = input.substring(1).trim();
            r = parseE();
            input = input.substring(1).trim();
        } else {
            throw new IllegalArgumentException("Invalid input: " + input);
        }
        return r;
    }
}
    


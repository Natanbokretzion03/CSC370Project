/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package semanticanalyzer;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Natan
 */
public class SemanticAnalyzer {
public static void main(String[] args) {
          SemanticAnalyzer analyzer = new SemanticAnalyzer();
   analyzer.analyze("1+ x-3* y");
    }

   private Map<String, Double> variables = new HashMap<>();

    public void analyze(String input) {
        String[] tokens = input.split("");

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (isNumeric(token)) {
                continue;
            } else if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                continue;
            } else if (variables.containsKey(token)) {
                continue;
            } else if (i > 0 && (tokens[i-1].equals("+") || tokens[i-1].equals("-") || tokens[i-1].equals("*") || tokens[i-1].equals("/"))) {
                variables.put(token, null);
            } else {
                System.out.println("Undefined variable: " + token);
            }
        }
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

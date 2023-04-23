/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package compiler;
import java.util.*;
import java.io.*;
/**
 *
 * @author Natan
 */
class Node {
   private final NodeT type;
    private final String value;

    public Node(NodeT type, String value) {
        this.type = type;
        this.value = value;
    }

    public NodeT getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Node(type=" + type + ", value=" + value + ")";
    }
}
enum NodeT {
    VAR,
    ASSIGN,
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    NUM;

    public static NodeT fromString(String s) {
        switch (s) {
            case "+":
                return ADD;
            case "-":
                return SUBTRACT;
            case "*":
                return MULTIPLY;
            case "/":
                return DIVIDE;
            default:
                return null;
        }
    }
}

public class Compiler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            String code = "x = 5 * 3 + 2 - 1";
        System.out.println("Input code: " + code);

        List<Node> ast = parseCode(code);
        System.out.println("AST: " + ast);

        List<String> intermediateCode = generateIntermediateCode(ast);
        System.out.println("Intermediate code: " + intermediateCode);

        List<String> machineCode = generateMachineCode(intermediateCode);
        System.out.println("Machine code: " + machineCode);
    }

    private static List<Node> parseCode(String code) {
     List<Node> ast = new ArrayList<>();
        String[] tokens = code.split(" ");
        
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            
            if (Character.isLetter(token.charAt(0))) {
                // Variable
                ast.add(new Node(NodeT.VAR, token));
            } else if (Character.isDigit(token.charAt(0))) {
                // Number
                ast.add(new Node(NodeT.NUM, token));
            } else if (token.equals("+")) {
                ast.add(new Node(NodeT.ADD, null));
            } else if (token.equals("-")) {
                ast.add(new Node(NodeT.SUBTRACT, null));
            } else if (token.equals("*")) {
                ast.add(new Node(NodeT.MULTIPLY, null));
            } else if (token.equals("/")) {
                ast.add(new Node(NodeT.DIVIDE, null));
            } else if (token.equals("=")) {
                ast.add(new Node(NodeT.ASSIGN, null));
            }
        }
        
        return ast;
    }

    private static List<String> generateIntermediateCode(List<Node> ast) {
        List<String> intermediateCode = new ArrayList<>();
        int tempVarCount = 1;

        for (int i = 0; i < ast.size(); i++) {
            Node currentNode = ast.get(i);

            if (currentNode.getType() == NodeT.MULTIPLY ||currentNode.getType() == NodeT.DIVIDE) {
                Node leftNode = ast.get(i - 1);
                Node rightNode = ast.get(i + 1);
                String tempVar = "t" + tempVarCount;

                intermediateCode.add(tempVar + " = " + leftNode.getValue() + " " + currentNode.getType().toString().toLowerCase() + " " 
                        + rightNode.getValue());

                ast.set(i - 1, new Node(NodeT.NUM, tempVar));
                ast.remove(i);
                ast.remove(i);

                tempVarCount++;
                i -= 2;
            }
        }

        for (int i = 1; i < ast.size(); i++) {
            Node currentNode = ast.get(i);

            if (currentNode.getType() == NodeT.ADD || currentNode.getType() == NodeT.SUBTRACT) {
                Node leftNode = ast.get(i - 1);
                Node rightNode = ast.get(i + 1);
                String tempVar = "t" + tempVarCount;

                intermediateCode.add(tempVar + " = " + leftNode.getValue() + " " + currentNode.getType().toString().toLowerCase() + " " + 
                        rightNode.getValue());

                ast.set(i - 1, new Node(NodeT.NUM, tempVar));
                ast.remove(i);
                ast.remove(i);

                tempVarCount++;
                i -= 2;
            }
        }

        Node assignmentNode = ast.get(2);
        String finalCode =   ast.get(0).getValue()+" = "+assignmentNode.getValue();
        intermediateCode.add(finalCode);

        return intermediateCode;
    }

    private static List<String> generateMachineCode(List<String> intermediateCode) {
      List<String> machineCode = new ArrayList<>();

    int registerNum = 1;
    for (String code : intermediateCode) {
        String[] tokens = code.split(" ");

        if (tokens.length == 5) {
            String opCode = "";
            if (tokens[1].equals("=")) {
                if (tokens[3].equals("+")) {
                    opCode = "add";
                } else if (tokens[3].equals("-")) {
                    opCode = "sub";
                } else if (tokens[3].equals("*")) {
                    opCode = "mul";
                } else if (tokens[3].equals("/")) {
                    opCode = "div";
                }
                machineCode.add(opCode + " r" + registerNum + ", #" + tokens[4]);
                machineCode.add("mov " + tokens[0] + ", r" + registerNum);
                registerNum++;
            } else if (tokens[1].equals("*")) {
                machineCode.add("mov r" + registerNum + ", #" + tokens[2]);
                machineCode.add("mov r" + (registerNum + 1) + ", #" + tokens[4]);
                machineCode.add("mul r" + registerNum + ", r" + registerNum + ", r" + (registerNum + 1));
                registerNum += 2;
            } else if (tokens[1].equals("+")) {
                machineCode.add("mov r" + registerNum + ", #" + tokens[2]);
                machineCode.add("mov r" + (registerNum + 1) + ", #" + tokens[4]);
                machineCode.add("add r" + registerNum + ", r" + registerNum + ", r" + (registerNum + 1));
                machineCode.add("mov " + tokens[0] + ", r" + registerNum);
                registerNum += 2;
            }
        }
    }

    return machineCode;
}
}
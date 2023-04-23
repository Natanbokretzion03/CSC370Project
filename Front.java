/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package front;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter;


public class Front {
    
    static int charClass;
    static String lexeme = "";
    static char nextChar;
    static int lexLen;
    static int token;
    static int nextToken;
    static BufferedReader in_fp;

    static final int LETTER = 0;
    static final int DIGIT = 1;
    static final int UNKNOWN = 99;


    static final int INT_LIT = 10;
    static final int IDENT = 11;
    static final int ASSIGN_OP = 20;
    static final int ADD_OP = 21;
    static final int SUB_OP = 22;
    static final int MULT_OP = 23;
    static final int DIV_OP = 24;
    static final int LEFT_PAREN = 25;
    static final int RIGHT_PAREN = 26;
    static final int EOF = -1;
    
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("write an SPL equation:");
       String userInput = scanner.nextLine();
    
     
        try {
       
            FileWriter fileWriter = new FileWriter("input.txt");
            fileWriter.write(userInput);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
      
        try {
            in_fp = new BufferedReader(new FileReader("input.txt"));
            getChar();
            do {
                lex();
            } while (nextToken != EOF);
        } catch (IOException e) {
            System.out.println("Error opening input file");
        }
    }
   
     public static int lookup(char ch) {
        switch (ch) {
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+':
                addChar();
                nextToken = ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = SUB_OP;
                break;
            case '*':
                addChar();
                nextToken = MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = DIV_OP;
                break;
            default:
                addChar();
                nextToken = EOF;
                break;
        }
        return nextToken;
    }


    public static void addChar() {
        if (lexLen <= 98) {
            lexeme += nextChar;
            lexLen++;
        } else {
            System.out.println("Error - lexeme is too long");
        }
    }


    public static void getChar() {
        try {
            int c = in_fp.read();
            if (c != -1) {
                nextChar = (char) c;
                if (Character.isLetter(nextChar))
                    charClass = LETTER;
                else if (Character.isDigit(nextChar))
                    charClass = DIGIT;
                else charClass = UNKNOWN;
            } else {
                charClass = EOF;
            }
        } catch (IOException e) {
            System.out.println("Error - cannot read character");
        }
    }


    public static void getNonBlank() {
        while (Character.isWhitespace(nextChar)) {
            getChar();
        }
    }

     public static void lex() {
         
  lexLen = 0;
    getNonBlank();
    switch (charClass) {
      
        case LETTER:
            addChar();
            getChar();
            while (charClass == LETTER || charClass == DIGIT) {
                addChar();
                getChar();
            }
            nextToken = IDENT;
            break;
     
        case DIGIT:
            addChar();
            getChar();
            while (charClass == DIGIT) {
                addChar();
                getChar();
            }
            nextToken = INT_LIT;
            break;
       
        case UNKNOWN:
            lookup(nextChar);
            getChar();
            break;
        /* EOF */
        case EOF:
            nextToken = EOF;
           lexeme = "EOF";
            break;
            
    }
    /* End of switch */
    System.out.printf("Next token is: %d, Next lexeme is %s\n",
        nextToken, lexeme);
   
     }
}

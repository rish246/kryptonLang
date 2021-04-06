package com.Rishabh;


import java.util.Scanner;


public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";


    public static void main(String[] args) {

        boolean displayParseTree = false;


	// write your code here
        while (true) {

            System.out.print("Krypton >> ");
            String line = scanner.nextLine().trim();

            if(line.equals("#dispTree")) {
                displayParseTree = !displayParseTree;
                continue;
            }

            Parser parser = new Parser(line);
            Expression result = parser.parse(0);

            try {

                if (parser._diagnostics.size() > 0) {
                    // Print the errors and continue with our loop
                    for(String diagnostic : parser._diagnostics)
                        System.out.println(TEXT_RED + diagnostic + TEXT_RESET);

                    continue;
                }
                
                if(displayParseTree) 
                    result.prettyPrint("");

                int answer = (int) result.evaluate();
                System.out.println(answer);
            } catch (Exception e1) {
                System.out.println(e1.toString());
            }
        }
    }



}

// Adding parser diagnostics


// Parse strings and return Tokens


package com.Rishabh;


import com.Rishabh.Expression.Expression;
import com.Rishabh.Utilities.Environment;

import java.util.List;
import java.util.Scanner;


public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";


    public static void main(String[] args) {

        boolean displayParseTree = false;

        // Create an environment here
        Environment parentEnv = new Environment();

	// write your code here
        while (true) {

            System.out.print("Krypton >> ");
            String line = scanner.nextLine().trim();

            if(line.equals(""))
                continue;

            if(line.equals("#dispTree")) {
                displayParseTree = !displayParseTree;
                continue;
            }

            if(line.startsWith("#"))
                continue;

            Parser parser = new Parser(line);
            parser.printTokens();
            Expression result = parser.parse();
            if(displayParseTree)
                result.prettyPrint("");
//
            try {

//                result.prettyPrint("");

                if (parser._diagnostics.size() > 0) {

                    for(String diagnostic : parser._diagnostics)
                        System.out.println(TEXT_RED + diagnostic + TEXT_RESET);

                    continue;
                }

                if(displayParseTree)
                    result.prettyPrint("");

                EvalResult answer = result.evaluate(parentEnv);
                List<String> runtimeDiagnostics = result.getDiagnostics();

                if(runtimeDiagnostics.size() > 0) {
                    for(String diagnostic : runtimeDiagnostics) {
                        System.out.println(TEXT_RED + diagnostic + TEXT_RESET);
                    }
                    continue;
                }


                System.out.println(answer._value);
            } catch (Exception e1) {

                System.out.println(e1.toString());

            }
//
        }


    }



}


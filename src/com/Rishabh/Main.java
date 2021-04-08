package com.Rishabh;


import com.Rishabh.Expression.Expression;
import com.Rishabh.Utilities.Environment;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";


    public static void main(String[] args) {

        boolean displayParseTree = false;

        // Create an environment here
        Environment parentEnv = new Environment(null);

	// write your code here
        while (true) {

            System.out.print("Krypton >> ");
            String line = scanner.nextLine().trim();
//            int LineNumber = 1;


            boolean isValidInput = hasValidParens(line);
            while(!isValidInput) {
                System.out.print(">>>\t");
                String nextLine = scanner.nextLine().trim();
                line = line + nextLine;
                isValidInput = hasValidParens(line);
            }

            // ExpressionBlock -> {ListOfExpressions separated by newLine}
            // SplitByNewLine_.
            // List<Expression>
            // For Each Expression, Evaluate() and printErrors()
            // {, }, ;
            // {
            //   --> (SplitTerms and then Parse)
            //   --> (ParseStatement)
            //   --> Create a new Env with parent as provided env()
            //   ==> // ParseBlock ({  ListOfExpressions  })
            //      --> New Kind of Exp -> Block Expression
            //      --> Block And The Parse Expression -->
            // }


            if(line.equals(""))
                continue;

            if(line.equals("#dispTree")) {
                displayParseTree = !displayParseTree;
                continue;
            }

            if(line.startsWith("#"))
                continue;

            Parser parser = new Parser(line);
//            parser.printTokens();
            Expression result = parser.parse();
            if(displayParseTree)
                result.prettyPrint("");
////
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

        }
    }



    public static boolean hasValidParens(String inputLine) {
        int netRes = 0;
        // Take ( == 1 and ) == -1.. after exec, the netRes should be 0
        HashMap<Character, Integer> score = new HashMap<Character, Integer>();
        score.put('(', 1);
        score.put(')', -1);
        score.put('{', 2);
        score.put('}', -2);


        char[] nextLine = inputLine.toCharArray();
        for(char ch : nextLine) {
            if(score.containsKey(ch))
                netRes += score.get(ch);
        }

        return (netRes == 0);
    }



}


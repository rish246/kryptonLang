package com.Rishabh;


import com.Rishabh.Utilities.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_GREEN = "\u001B[32m";


    
    public static void main(String[] args) {
        // Evaluate parse Tree -> generate parse Tree and evaluate the parse Tree
        // For file... if(args[]) {}
        // Execute file
        //      parser
        //      result
        //      evaluate
        if(args.length == 0) {
            runRepl();
        }
        else {

            Environment programEnv = new Environment(null);
            String filename = args[0];
            try {
                String program = readInputFile(filename);
                SyntaxTree result = parseProgram(program);

                // Line numbers are not being maintined for some reason
                if(result == null)
                    return;


                EvalResult answer = getEvalResult(programEnv, result);
                if (answer == null || answer._value == null)
                    return;

                System.out.println(TEXT_GREEN + answer._value + TEXT_RESET);
                // evaluate the result

            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }


        }
    }

    private static EvalResult getEvalResult(Environment programEnv, SyntaxTree result) throws Exception {
        EvalResult answer = result.evaluate(programEnv);
        List<String> runtimeDiagnostics = result.getDiagnostics();

        if (runtimeDiagnostics.size() > 0) {
            printDiagnostics(runtimeDiagnostics);
            return null;
        }
        return answer;
    }

    private static SyntaxTree parseProgram(String program) {
        Parser parser = new Parser(program);
        SyntaxTree result = parser.parse();

        if(parser._diagnostics.size() > 0) {
            printDiagnostics(parser._diagnostics);
            return null;
        }
        return result;
    }


    private static void runRepl() {
        boolean displayParseTree = false;

        // Create an environment here
        Environment parentEnv = new Environment(null);


        // write your code here
        while (true) {

            System.out.print("Krypton >> ");
            String line = scanner.nextLine().trim();

            if (line.equals("#dispTree")) {
                displayParseTree = !displayParseTree;
                continue;
            }



            SyntaxTree result;
            Parser parser;

            try {

                if (line.startsWith("load")) {
                    String filename = line.substring(5);
                    String programCode = readInputFile(filename);

                    parser = new Parser(programCode);

                    result = parser.parse();
                }
                else {
                    line = getNextCodeSegment(line);

                    if (line.equals(""))
                        continue;

                    parser = new Parser(line);
                    result = parser.parse();

                }

                if (displayParseTree)
                    result.prettyPrint("");

                if (parser._diagnostics.size() > 0) {
                    printDiagnostics(parser._diagnostics);
                    continue;
                }


                EvalResult answer = getEvalResult(parentEnv, result);
                if (answer == null || answer._value == null)
                    continue;

                System.out.println(TEXT_GREEN + answer._value + ", " + answer._type + TEXT_RESET);

            } catch (Exception e1) {
                System.out.println(e1.toString());
            }

        }
    }

    private static void printDiagnostics(List<String> Diagnostics) {
        for (String diagnostic : Diagnostics)
            System.out.println(TEXT_RED + diagnostic + TEXT_RESET);
    }

    private static String getNextCodeSegment(String line) {
        boolean isValidInput = hasValidParens(line);
        while (!isValidInput) {
            System.out.print("\t");
            String nextLine = scanner.nextLine().trim();
            line = line + '\n' + nextLine;
            isValidInput = hasValidParens(line);
        }
        return line;
    }

    private static String readInputFile(String myInputFile) throws FileNotFoundException {
        File inputFileObj = new File(myInputFile);
        Scanner inputFileReader = new Scanner(inputFileObj);

        String programCode = "";
        while (inputFileReader.hasNextLine()) {
            String nextLineFile = inputFileReader.nextLine().trim();
            programCode += nextLineFile + '\n';
        }
        return programCode;
    }


    public static boolean hasValidParens(String inputLine) {
        int netRes = 0;
        // Take ( == 1 and ) == -1.. after exec, the netRes should be 0
        HashMap<Character, Integer> score = new HashMap<>();
        score.put('(', 1);
        score.put(')', -1);
        score.put('{', 1);
        score.put('}', -1);
        score.put('[', 1);
        score.put(']', -1);




        char[] nextLine = inputLine.toCharArray();
        for(char ch : nextLine) {
            if(score.containsKey(ch))
                netRes += score.get(ch);
        }

        return (netRes == 0);
    }



}

// Statement... Which extend SyntaxTree(Type) {
        // Just like Expression

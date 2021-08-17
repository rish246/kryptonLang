package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadInputExpression extends Expression {
    String _type;
    String _prompt;
    List<String> _diagnostics = new ArrayList<>();

    public ReadInputExpression(String type, String prompt, int lineNumber) {
        super(ExpressionType.ReadInputExpression, lineNumber);
        _type = type;
        _prompt = prompt;
    }


    @Override
    public EvalResult evaluate(Environment env) throws Exception {
        Scanner inputScanner = new Scanner(System.in);
        System.out.print(_prompt);
        String input = inputScanner.nextLine();

        if(_type.equals("int")) {
            return new EvalResult(Integer.parseInt(input), "int");
        }

        return new EvalResult(input, "string");
    }

    @Override
    public void prettyPrint(String indent) {
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}

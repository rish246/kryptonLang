package com.Krypton.Utilities;

import com.Krypton.EvalResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Printer {

    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_GREEN = "\u001B[32m";

    public static void print(EvalResult resultToPrint) {
        Object printableValue = Printer.getPrintableValue(resultToPrint);
        System.out.println(TEXT_GREEN + printableValue.toString() + TEXT_RESET);
    }

    public static Object getPrintableValue(EvalResult bodyOp) {
        if(bodyOp._type == "list") {
            List<Object> printableList = new ArrayList<>();
            List<EvalResult> list = (List) bodyOp._value;
            for(EvalResult nextElement : list) {
                printableList.add(getPrintableValue(nextElement));
            }
            return printableList;
        }

        else if(bodyOp._type == "object") {
            Map<String, Object> printableObject = new HashMap<>();
            Map<Object, EvalResult> object = (HashMap) bodyOp._value;

            for(Map.Entry<Object, EvalResult> binding : object.entrySet()) {
                Object printableValue = getPrintableValue(binding.getValue());
                printableObject.put(binding.getKey().toString(), printableValue);
            }
            return printableObject;

        }
        else if(bodyOp._type == "null") {
            return "null";
        }
        else {
            return bodyOp._value;
        }
    }
    
}

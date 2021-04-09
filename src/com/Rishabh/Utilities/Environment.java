package com.Rishabh.Utilities;

import java.util.HashMap;

public class Environment {
    private final HashMap<String, Symbol> table = new HashMap<>();
    private final Environment _ParentEnv;


    public Environment(Environment env) {
        _ParentEnv = env;
    }

    public Symbol set(String lexeme, Symbol entry) {
        // If entry already exists in the parent scope, update that ... else add a new entry
        for(Environment curEnv = this; curEnv != null; curEnv = curEnv._ParentEnv) {
            if(curEnv.table.containsKey(lexeme)) {
                // Update the entry in the curEnv
                curEnv.table.put(lexeme, entry);
                return entry;
            }
        }

        table.put(lexeme, entry);
        return entry;
    }

    public Symbol get(String lexeme) {
        for(Environment curEnv = this; curEnv != null; curEnv = curEnv._ParentEnv) {
            if(curEnv.table.containsKey(lexeme))
                return curEnv.table.get(lexeme);
        }
        return null;
    }

    public void printEnv() {
        System.out.println("{");

        for(String key : table.keySet()) {
            System.out.println("{ " + key + " : <" + table.get(key)._value + ">}");
        }

        System.out.println("}");

    }
}


// Debugging the Interpreter: I Am All Alone IIIIIIIIIAMALLLYOURSYOUTEASINGME

package com.Rishabh.Utilities;

import java.util.HashMap;

public class Environment {
    public HashMap<String, Symbol> _table = new HashMap<>();
    public Environment _ParentEnv;


    public Environment(Environment env) {
        _ParentEnv = env;
    }

    public Environment(HashMap<String, Symbol> table, Environment parentEnv) {
        _table = table;
        _ParentEnv = parentEnv;
    }


    public Symbol set(String lexeme, Symbol entry) {
        // If entry already exists in the parent scope, update that ... else add a new entry
        for(Environment curEnv = this; curEnv != null; curEnv = curEnv._ParentEnv) {
            if(curEnv._table.containsKey(lexeme)) {
                curEnv._table.put(lexeme, entry);
                return entry;
            }
        }

        _table.put(lexeme, entry);
        return entry;
    }

    public Symbol get(String lexeme) {
        for(Environment curEnv = this; curEnv != null; curEnv = curEnv._ParentEnv) {
            if(curEnv._table.containsKey(lexeme))
                return curEnv._table.get(lexeme);
        }
        return null;
    }

    public void printEnv() {
        System.out.println("{");

        for(String key : _table.keySet()) {
            System.out.println("{ " + key + " : <" + _table.get(key)._value + ">}");
        }

        System.out.println("}");

    }
}


// Debugging the Interpreter: I Am All Alone IIIIIIIIIAMALLLYOURSYOUTEASINGME

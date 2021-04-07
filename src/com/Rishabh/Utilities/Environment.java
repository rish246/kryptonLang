package com.Rishabh.Utilities;

import com.Rishabh.Expression.IdentifierExpression;

import java.util.HashMap;

public class Environment {
    private final HashMap<String, Symbol> table = new HashMap<>();
    private final Environment _ParentEnv;

    public Environment(Environment env) {
        _ParentEnv = env;
    }

    public Symbol addEntry(String lexeme, Symbol entry) {
        table.put(lexeme, entry);
//        System.out.println(entry);
        return entry;
        // else create a new one and return that
    }

    public Symbol getEntry(String lexeme) {
        if (table.containsKey(lexeme)) return table.get(lexeme);

        if(_ParentEnv != null)
            return _ParentEnv.getEntry(lexeme);
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

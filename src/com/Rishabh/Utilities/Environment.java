package com.Rishabh.Utilities;

import com.Rishabh.Expression.IdentifierExpression;

import java.util.HashMap;

public class Environment {
    private final HashMap<String, Symbol> table = new HashMap<>();

    public Symbol addEntry(String lexeme, Symbol entry) {
        table.put(lexeme, entry);
//        System.out.println(entry);
        return entry;
        // else create a new one and return that
    }

    public Symbol getEntry(String lexeme) {
        if (table.containsKey(lexeme)) {
            return table.get(lexeme);
        }
        return null;
    }
}

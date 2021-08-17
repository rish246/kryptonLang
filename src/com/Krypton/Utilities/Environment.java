package com.Krypton.Utilities;

import com.Krypton.EvalResult;

import java.util.HashMap;

public class Environment {
    public HashMap<String, EvalResult> _table = new HashMap<>();
    public Environment _ParentEnv;


    public Environment(Environment env) {
        _ParentEnv = env;
    }

    public Environment(HashMap<String, EvalResult> table, Environment parentEnv) {
        _table = table;
        _ParentEnv = parentEnv;
    }

    public static Environment copy(Environment env) {
        Environment newEnvironment = new Environment(env._ParentEnv);
        newEnvironment._table = new HashMap<String, EvalResult>(env._table);
        return newEnvironment;
    }

    public EvalResult set(String lexeme, EvalResult entry) {
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

    public EvalResult get(String lexeme) {
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

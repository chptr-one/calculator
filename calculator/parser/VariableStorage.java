package calculator.parser;

import calculator.exceptions.UnknownVariableException;

import java.util.HashMap;
import java.util.Map;

public class VariableStorage {
    private final static VariableStorage instance = new VariableStorage();
    private final Map<String, Double> pool = new HashMap<>();

    private VariableStorage() {
    }

    static VariableStorage getInstance() {
        return instance;
    }

    void setVariable(String name, Double value) {
        if (pool.containsKey(name)) {
            pool.replace(name, value);
        } else {
            pool.put(name, value);
        }
    }

    double getValue(String name) throws UnknownVariableException {
        if (pool.containsKey(name)) {
            return pool.get(name);
        } else {
            throw new UnknownVariableException("Unknown variable");
        }
    }
}

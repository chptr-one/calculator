import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")

public enum LexemeType {
    NUMBER,
    OPENING_BRACKET,
    CLOSING_BRACKET,
    ADD,
    SUBSTRACT,
    MULTIPLY,
    DIVIDE,
    UNARY_MINUS,
    END;


    public static final Map<String, LexemeType> LEXEMES;

    static {
        LEXEMES = new HashMap<>();
        LEXEMES.put("(", OPENING_BRACKET);
        LEXEMES.put(")", CLOSING_BRACKET);
        LEXEMES.put("+", ADD);
        LEXEMES.put("-", SUBSTRACT);
        LEXEMES.put("*", MULTIPLY);
        LEXEMES.put("/", DIVIDE);
        LEXEMES.put("minus", UNARY_MINUS);
    }

    public double action(double x, double y) {
        switch (this) {
            case ADD:
                return x + y;
            case SUBSTRACT:
                return x - y;
            case MULTIPLY:
                return x * y;
            case DIVIDE:
                return x / y;
            default:
                return 0;
        }
    }

    public double action(double x) {
        return -x;
    }
}

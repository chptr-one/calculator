import java.util.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum LexemeType {
    NUMBER,
    OPENING_BRACKET,
    CLOSING_BRACKET,
    BINARY_OPERATOR,
    UNARY_OPERATOR,
    END
}

class Lexeme {
    LexemeType type;
    String value;

    Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" + type + " " + value + "}";
    }
}

class BinaryOperator extends Lexeme {
    DoubleBinaryOperator operator;

    public BinaryOperator(String value, DoubleBinaryOperator operator) {
        super(LexemeType.BINARY_OPERATOR, value);
        this.operator = operator;
    }
}

class UnaryOperator extends Lexeme {
    DoubleUnaryOperator operator;

    public UnaryOperator(String value, DoubleUnaryOperator operator) {
        super(LexemeType.UNARY_OPERATOR, value);
        this.operator = operator;
    }
}

class Lexer {
    static final String NUMBER_REG_EX = "[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
    static final Map<String, Lexeme> LEXEMES;

    static {
        LEXEMES = new HashMap<>();
        LEXEMES.put("(", new Lexeme(LexemeType.OPENING_BRACKET,"("));
        LEXEMES.put(")", new Lexeme(LexemeType.CLOSING_BRACKET,")"));
        // LEXEMES.put("minus", new UnaryOperator("-", (d1) -> -d1));
        LEXEMES.put("+", new BinaryOperator("+", Double::sum));
        LEXEMES.put("-", new BinaryOperator("-", (d1, d2) -> d1 - d2));
        LEXEMES.put("*", new BinaryOperator("*", (d1, d2) -> d1 * d2));
        LEXEMES.put("/", new BinaryOperator("/", (d1, d2) -> d1 / d2));
    }

    static List<Lexeme> parse(String s) {
        List<Lexeme> lexemes = new LinkedList<>();
        s = s.toLowerCase().replaceAll("\\s", "");
        Matcher matcher = Pattern.compile(NUMBER_REG_EX).matcher(s);

        int pos = 0;
        String value;
        while (pos < s.length()) {
            value = "";
            if (s.charAt(pos) >= '0' && s.charAt(pos) <= '9') {
                if (matcher.find()) {
                    value = matcher.group();
                    pos += value.length();
                    lexemes.add(new Lexeme(LexemeType.NUMBER, value));
                } else {
                    throw new IllegalArgumentException("Wrong number in " + s + "  at pos " + pos);
                }
            } else {
                value += s.charAt(pos);
                if (value.equals("-")) {
                    LexemeType prevLexemeType = (lexemes.size() > 0) ? lexemes.get(lexemes.size() - 1).type : null;
                    if ((prevLexemeType != LexemeType.NUMBER) && (prevLexemeType != LexemeType.CLOSING_BRACKET))
                        value = "minus";
                }
                if (LEXEMES.containsKey(value))
                    lexemes.add(LEXEMES.get(value));
                else
                    throw new IllegalArgumentException("Wrong symbol " + value + " in " + s + " at pos " + pos);
                pos++;
            }
        }
        lexemes.add(new Lexeme(LexemeType.END,""));
        return lexemes;
    }

    public static void main(String[] args) {
        String expr = "-(0.5e2 + 1) * 2 - 3";
        System.out.println(parse(expr));
    }
}

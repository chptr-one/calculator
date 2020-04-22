import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Lexer {
    static final String NUMBER_REG_EX = "[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";

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
                    LexemeType prevLexemeType = (lexemes.size() > 0) ? lexemes.get(lexemes.size() - 1).getType() : null;
                    if ((prevLexemeType != LexemeType.NUMBER) && (prevLexemeType != LexemeType.CLOSING_BRACKET))
                        value = "minus";
                }
                if (LexemeType.LEXEMES.containsKey(value))
                    lexemes.add(new Lexeme(LexemeType.LEXEMES.get(value), value));
                else
                    throw new IllegalArgumentException("Wrong symbol " + value + " in " + s + " at pos " + pos);
                pos++;
            }
        }
        lexemes.add(new Lexeme(LexemeType.END, ""));
        return lexemes;
    }

    public static void main(String[] args) {
        String expr = "-(0.5e2 + 1) * 2 - 3";
        System.out.println(parse(expr));
    }
}

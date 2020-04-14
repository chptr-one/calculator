/*
NUMBER  -> <число>
group   -> "("  add  ")" | NUMBER
mult    -> group (( "*" | "/" ) group)*
add     -> mult (( "+" | "-" ) mult)*
result  -> add
*/

import java.util.List;
// TODO add an unary operators
class Parser {
    List<Lexeme> lexemes;
    int pos;

    public Parser(String expr) {
        this.lexemes = Lexer.parse(expr);
        pos = 0;
    }

    double result() {
        Lexeme lexeme = lexemes.get(pos);
        return (lexeme.type == LexemeType.END) ? Double.NaN : add();
    }

    double number() {
        Lexeme lexeme = lexemes.get(pos);
        if (lexeme.type == LexemeType.NUMBER) {
            pos++;
            return Double.parseDouble(lexeme.value);
        } else
            throw new IllegalArgumentException("Expected NUMBER");
    }

    double group() {
        Lexeme lexeme = lexemes.get(pos);
        if (lexeme.type == LexemeType.OPENING_BRACKET) {
            pos++;
            double result = add();
            lexeme = lexemes.get(pos);
            if (lexeme.type == LexemeType.CLOSING_BRACKET) {
                pos++;
                return result;
            } else throw new IllegalArgumentException("Expected )");
        } else {
            return number();
        }
    }

    double mult() {
        double result = group();
        Lexeme lexeme = lexemes.get(pos);
        while (lexeme.type == LexemeType.BINARY_OPERATOR && lexeme.value.matches("[*|/]")) {
            pos++;
            result = ((BinaryOperator) lexeme).operator.applyAsDouble(result, group());
            lexeme = lexemes.get(pos);
        }
        return result;
    }

    double add() {
        double result = mult();
        Lexeme lexeme = lexemes.get(pos);
        while (lexeme.type == LexemeType.BINARY_OPERATOR && lexeme.value.matches("[+|-]")) {
            pos++;
            result = ((BinaryOperator) lexeme).operator.applyAsDouble(result, mult());
            lexeme = lexemes.get(pos);
        }
        return result;
    }

    public static void main(String[] args) {
        String expr = "(2 * 3 - (2 - 1) * 2) / 2";
        Parser parser = new Parser(expr);
        System.out.println(parser.result());
    }
}

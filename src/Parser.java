/*
NUMBER  -> <число>
group   -> "("  add  ")" | NUMBER
mult    -> group (( "*" | "/" ) group)*
add     -> mult (( "+" | "-" ) mult)*
result  -> add
*/

import java.util.List;

class Parser {
    List<Lexeme> lexemes;
    int pos;

    public Parser(String expr) {
        this.lexemes = Lexer.parse(expr);
        pos = 0;
    }

    double result() {
        Lexeme lexeme = lexemes.get(pos);
        return (lexeme.getType() == LexemeType.END) ? Double.NaN : add();
    }

    double number() {
        Lexeme lexeme = lexemes.get(pos);
        if (lexeme.getType() == LexemeType.NUMBER) {
            pos++;
            return Double.parseDouble(lexeme.getValue());
        } else
            throw new IllegalArgumentException("Expected NUMBER");
    }

    double group() {
        Lexeme lexeme = lexemes.get(pos);
        if (lexeme.getType() == LexemeType.OPENING_BRACKET) {
            pos++;
            double result = add();
            lexeme = lexemes.get(pos);
            if (lexeme.getType() == LexemeType.CLOSING_BRACKET) {
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
        while (lexeme.getType() == LexemeType.MULTIPLY || lexeme.getType() == LexemeType.DIVIDE ) {
            pos++;
            result = lexeme.getType().action(result, group());
            lexeme = lexemes.get(pos);
        }
        return result;
    }

    double add() {
        double result = mult();
        Lexeme lexeme = lexemes.get(pos);
        while (lexeme.getType() == LexemeType.ADD || lexeme.getType() == LexemeType.SUBSTRACT) {
            pos++;
            result = lexeme.getType().action(result, mult());
            lexeme = lexemes.get(pos);
        }
        return result;
    }
}

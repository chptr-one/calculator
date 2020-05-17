package calculator.parser;

/*
    expression -> add [EOF]
    add -> mult ([+|-] mult)*
    mult -> group ([*|/] group)*
    group   -> [(] add [)] | number
    number -> ([+|-])* [double | VARIABLE]
    double -> "[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"
    VARIABLE -> "[a-zA-Z]+"
 */

import calculator.exceptions.TokenException;
import calculator.exceptions.VariableException;

public class Calculator {
    public final VariableStorage variables = VariableStorage.getInstance();
    private final Tokenizer tokenizer = new Tokenizer();

    public void setVariable(String input) throws VariableException {
        final String VAR_REGEX = "[a-zA-Z]+";
        int splitPosition = input.indexOf("=");
        String name = input.substring(0, splitPosition).trim();

        if (name.matches(VAR_REGEX)) {
            String expression = input.substring(splitPosition + 1).trim();
            Calculator calc = new Calculator();
            try {
                Double value = calc.calculate(expression);
                variables.setVariable(name, value);
            } catch (TokenException | VariableException e) {
                System.out.println(e.getMessage());
            }
        } else
            throw new VariableException("Invalid identifier");
    }

    public double calculate(String input) throws TokenException, VariableException {
        tokenizer.parseExpression(input);
        return expression();
    }

    private double expression() throws TokenException, VariableException {
        double result = add();
        if (tokenizer.hasNext())
            throw new TokenException("Invalid expression.");
        return result;
    }

    private double add() throws TokenException, VariableException {
        double result = mult();
        while (tokenizer.hasNext(TokenType.ADD, TokenType.SUBSTRACT)) {
            Token operator = tokenizer.getNext();
            double secondOperand = mult();
            if ("-".equals(operator.getValue()))
                secondOperand = -secondOperand;
            result += secondOperand;
        }
        return result;
    }

    private double mult() throws TokenException, VariableException {
        double result = group();
        while (tokenizer.hasNext(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            Token operator = tokenizer.getNext();
            double secondOperand = group();
            if ("/".equals(operator.getValue()))
                result /= secondOperand;
            else
                result *= secondOperand;
        }
        return result;
    }

    private double group() throws TokenException, VariableException {
        double result;
        if (tokenizer.hasNext(TokenType.BRACKET)) {
            tokenizer.getNext(TokenType.BRACKET);
            result = add();
            tokenizer.getNext(TokenType.BRACKET);
        } else {
            result = number();
        }
        return result;
    }

    private double number() throws TokenException, VariableException {
        int sign = 1;
        while (tokenizer.hasNext(TokenType.ADD, TokenType.SUBSTRACT)) {
            Token operator = tokenizer.getNext();
            if ("-".equals(operator.getValue())) {
                sign *= -1;
            }
        }

        if (tokenizer.hasNext(TokenType.NUMBER))
            return Double.parseDouble(tokenizer.getNext(TokenType.NUMBER).getValue()) * sign;
        else {
            try {
                return variables.getValue(tokenizer.getNext(TokenType.VARIABLE).getValue()) * sign;
            } catch (VariableException e) {
                throw new VariableException("Unknown variable");
            }
        }
    }
}

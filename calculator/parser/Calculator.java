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

import calculator.exceptions.InvalidIdentifierException;
import calculator.exceptions.UnknownTokenException;
import calculator.exceptions.UnknownVariableException;

public class Calculator {
    public final VariableStorage variables = VariableStorage.getInstance();
    private final Tokenizer tokenizer = new Tokenizer();

    public void setVariable(String input) throws InvalidIdentifierException {
        final String VAR_REGEX = "[a-zA-Z]+";
        int splitPosition = input.indexOf("=");
        String name = input.substring(0, splitPosition).trim();

        if (name.matches(VAR_REGEX)) {
            String expression = input.substring(splitPosition + 1).trim();
            Calculator calc = new Calculator();
            try {
                Double value = calc.calculate(expression);
                variables.setVariable(name, value);
            } catch (UnknownTokenException | UnknownVariableException e) {
                System.out.println(e.getMessage());
            }
        } else
            throw new InvalidIdentifierException("Invalid identifier");
    }

    public double calculate(String input) throws UnknownTokenException, UnknownVariableException {
        tokenizer.parseExpression(input);
        return expression();
    }

    private double expression() throws UnknownTokenException, UnknownVariableException {
        double result = add();
        if (tokenizer.hasNext())
            throw new UnknownTokenException("Invalid expression.");
        return result;
    }

    private double add() throws UnknownTokenException, UnknownVariableException {
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

    private double mult() throws UnknownTokenException, UnknownVariableException {
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

    private double group() throws UnknownTokenException, UnknownVariableException {
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

    private double number() throws UnknownTokenException, UnknownVariableException {
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
            } catch (UnknownVariableException e) {
                throw new UnknownVariableException("Unknown variable");
            }
        }
    }
}

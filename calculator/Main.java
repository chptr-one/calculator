package calculator;

import calculator.exceptions.InvalidIdentifierException;
import calculator.exceptions.UnknownTokenException;
import calculator.exceptions.UnknownVariableException;
import calculator.parser.Calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("Type \"/exit\" to exit");
        while (!"/exit".equals(input = sc.nextLine().trim())) {
            if (!input.isEmpty()) {
                if (input.contains("=")) {
                    try {
                        calc.setVariable(input);
                    } catch (InvalidIdentifierException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    try {
                        System.out.println(calc.calculate(input));
                    } catch (UnknownTokenException | UnknownVariableException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        System.out.println("Bye!");
    }
}
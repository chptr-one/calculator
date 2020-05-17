package calculator;

import calculator.exceptions.TokenException;
import calculator.exceptions.VariableException;
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
                    } catch (VariableException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    try {
                        System.out.println(calc.calculate(input));
                    } catch (TokenException | VariableException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        System.out.println("Bye!");
    }
}
public class Main {
    public static void main(String[] args) {
        String expr = "(2 * 3 - (3 - 1) * 2) / 0.5";
        Parser parser = new Parser(expr);
        System.out.println(expr + " = " + parser.result());
    }
}

@SuppressWarnings("unused")

public class Lexeme {
    private final LexemeType type;
    private final String value;

    Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
    }

    public LexemeType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{" + type + " " + value + "}";
    }
}

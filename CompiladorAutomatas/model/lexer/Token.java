package model.lexer;

/** Un token reconocido: tipo, texto y posición (línea y columna desde 1). */
public class Token {

    private final TokenType type;
    private final String lexeme;
    private final int line;
    private final int column;

    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() { return type; }
    public String getLexeme() { return lexeme; }
    public int getLine() { return line; }
    public int getColumn() { return column; }

    @Override
    public String toString() {
        return type.getLabel() + " '" + lexeme + "' (" + line + ":" + column + ")";
    }
}

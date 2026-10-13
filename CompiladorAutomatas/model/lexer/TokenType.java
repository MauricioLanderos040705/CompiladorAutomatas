package model.lexer;

/** Clasificación de los tokens del lenguaje. */
public enum TokenType {
    RESERVED_WORD("Palabra reservada"),
    IDENTIFIER("Identificador"),
    NUMBER("Número"),
    OPERATOR("Operador"),
    DELIMITER("Delimitador"),
    UNKNOWN("Desconocido");

    private final String label;

    TokenType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
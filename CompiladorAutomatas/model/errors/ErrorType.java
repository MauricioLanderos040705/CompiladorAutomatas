package model.errors;

/** Clasificación de los errores de compilación. */
public enum ErrorType {
    INVALID_IDENTIFIER("Identificador inválido"),
    INVALID_NUMBER("Número inválido"),
    UNKNOWN_SYMBOL("Símbolo desconocido");

    private final String label;

    ErrorType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
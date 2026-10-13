package model.symboltable;

/**
 * Un registro de la tabla de símbolos.
 * Es inmutable: una vez creado no cambia.
 */
public class Symbol {

    /**
     * Categoría del símbolo.
     * IMPORTANTE: no cambiar el orden, el archivo guarda la posición (ordinal).
     */
    public enum Category {
        RESERVED_WORD("Palabra reservada"),
        IDENTIFIER("Identificador"),
        INTEGER("Entero"),
        DECIMAL("Decimal");

        private final String label;

        Category(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final int id;            // posición de incorporación, desde 1
    private final String lexeme;     // texto del símbolo
    private final Category category;

    public Symbol(int id, String lexeme, Category category) {
        this.id = id;
        this.lexeme = lexeme;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return id + " | " + lexeme + " | " + category.getLabel();
    }
}
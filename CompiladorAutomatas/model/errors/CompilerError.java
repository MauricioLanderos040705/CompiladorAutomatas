package model.errors;

/** Un error de compilación: categoría, descripción y posición (línea y columna desde 1). */
public class CompilerError {

    private final ErrorType type;
    private final String description;
    private final int line;
    private final int column;

    public CompilerError(ErrorType type, String description, int line, int column) {
        this.type = type;
        this.description = description;
        this.line = line;
        this.column = column;
    }

    public ErrorType getType() { return type; }
    public String getDescription() { return description; }
    public int getLine() { return line; }
    public int getColumn() { return column; }

    @Override
    public String toString() {
        return type.getLabel() + " (" + line + ":" + column + "): " + description;
    }
}
package model.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Centraliza los errores detectados durante la compilación
 * y prepara el resumen que se muestra en la interfaz.
 */
public class ErrorManager {

    private final List<CompilerError> errors = new ArrayList<>();

    /** Registra un error al final de la lista (orden de detección). */
    public void report(ErrorType type, String description, int line, int column) {
        errors.add(new CompilerError(type, description, line, column));
    }

    public void clear() {
        errors.clear();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public int count() {
        return errors.size();
    }

    /** Devuelve los errores en el orden en que se detectaron (lista no modificable). */
    public List<CompilerError> getAll() {
        return Collections.unmodifiableList(errors);
    }

    /** Texto de resumen para la interfaz. */
    public String getSummary() {
        if (errors.isEmpty()) {
            return "Sin errores";
        }
        return errors.size() == 1
                ? "1 error encontrado"
                : errors.size() + " errores encontrados";
    }
}
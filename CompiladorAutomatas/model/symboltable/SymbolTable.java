package model.symboltable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Tabla de símbolos EN MEMORIA.
 * Conserva los registros en el orden en que se incorporan.
 * No escribe archivos: eso lo hará SymbolTableFile (punto 5).
 */
public class SymbolTable {

    private final List<Symbol> symbols = new ArrayList<>();

    /**
     * Agrega un símbolo al final de la tabla.
     * Política de duplicados: si el lexema ya existe, no se agrega.
     *
     * @return el símbolo creado, o null si el lexema ya estaba en la tabla
     */
    public Symbol add(String lexeme, Symbol.Category category) {
        if (contains(lexeme)) {
            return null;
        }
        Symbol symbol = new Symbol(symbols.size() + 1, lexeme, category);
        symbols.add(symbol);
        return symbol;
    }

    public boolean contains(String lexeme) {
        return find(lexeme) != null;
    }

    /** Devuelve el símbolo con ese lexema, o null si no existe. */
    public Symbol find(String lexeme) {
        for (Symbol symbol : symbols) {
            if (Objects.equals(symbol.getLexeme(), lexeme)) {
                return symbol;
            }
        }
        return null;
    }

    /** Devuelve todos los símbolos en orden de incorporación (lista no modificable). */
    public List<Symbol> getAll() {
        return Collections.unmodifiableList(symbols);
    }

    public int size() {
        return symbols.size();
    }

    public void clear() {
        symbols.clear();
    }
}

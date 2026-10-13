package model.keywords;

import java.util.List;

/**
 * Palabras reservadas del lenguaje (tema: tacos y salsas).
 * El orden de la lista es el orden de carga en la tabla de símbolos.
 */
public final class ReservedWords {

    private static final List<String> WORDS = List.of(
            // Tipos de datos
            "tacoBistec",    // int
            "tacoChorizo",   // float
            "tacoPastor",    // double
            "tacoSuadero",   // char
            "tacoCabeza",    // string
            "tacoPapa",      // boolean
            "tacoAdobado",   // void
            // Condicionales
            "guacamole",     // if
            "picoGallo",     // else
            // Bucles
            "salsaVerde",    // while
            "salsaRoja",     // for
            "salsaAmarilla"  // do
    );

    private ReservedWords() {
    }

    /** Devuelve las palabras reservadas en su orden de definición. */
    public static List<String> getAll() {
        return WORDS;
    }

    /** Indica si el texto es una palabra reservada (distingue mayúsculas). */
    public static boolean isReserved(String text) {
        for (String word : WORDS) {
            if (word.equals(text)) {
                return true;
            }
        }
        return false;
    }
}

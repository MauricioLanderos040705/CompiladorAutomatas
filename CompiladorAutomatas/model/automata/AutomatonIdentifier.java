package model.automata;

/**
 * AFD que reconoce identificadores.
 * Estados: q0 (inicial), q1 (aceptación), qE (error).
 * Alfabeto: L = letras y '_', D = dígitos, O = otro.
 * Solo reconoce; no guarda nada en la tabla de símbolos.
 */
public class AutomatonIdentifier {

    public static final int MAX_LENGTH = 40;

    private static final String Q0 = "q0";
    private static final String Q1 = "q1";
    private static final String QE = "qE";

    public AutomatonResult recognize(String input) {
        if (input == null || input.isEmpty()) {
            return AutomatonResult.rejected(input == null ? "" : input, Q0, 0,
                    "Cadena vacía: no es un identificador");
        }

        String state = Q0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            state = next(state, c);
            if (state.equals(QE)) {
                String msg = (i == 0 && isDigit(c))
                        ? "Un identificador no puede comenzar con un dígito"
                        : "Carácter no permitido en identificador: '" + c + "'";
                return AutomatonResult.rejected(input, QE, i, msg);
            }
        }

        // Aquí el estado es q1 (la cadena no es vacía y no hubo error).
        if (input.length() > MAX_LENGTH) {
            return AutomatonResult.rejected(input, state, MAX_LENGTH,
                    "El identificador excede " + MAX_LENGTH + " caracteres");
        }
        return AutomatonResult.accepted(input, state);
    }

    /** Función de transición δ(estado, carácter). */
    private String next(String state, char c) {
        switch (state) {
            case Q0:
                return isLetter(c) ? Q1 : QE;
            case Q1:
                return (isLetter(c) || isDigit(c)) ? Q1 : QE;
            default:
                return QE;
        }
    }

    private boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
package model.automata;

/**
 * AFD que reconoce enteros y decimales (con signo '-' opcional).
 * Estados: q0 (inicial), q1 (tras '-'), q2 (entero, acepta),
 *          q3 (tras '.'), q4 (decimal, acepta), qE (error).
 * Solo reconoce; no guarda nada en la tabla de símbolos.
 */
public class AutomatonNumber {

    public static final int MAX_LENGTH = 40;

    private static final String Q0 = "q0";
    private static final String Q1 = "q1";
    private static final String Q2 = "q2";
    private static final String Q3 = "q3";
    private static final String Q4 = "q4";
    private static final String QE = "qE";

    /** Estado final de un entero aceptado. */
    public static final String STATE_INTEGER = Q2;
    /** Estado final de un decimal aceptado. */
    public static final String STATE_DECIMAL = Q4;

    public AutomatonResult recognize(String input) {
        if (input == null || input.isEmpty()) {
            return AutomatonResult.rejected(input == null ? "" : input, Q0, 0,
                    "Cadena vacía: no es un número");
        }

        String state = Q0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            String previous = state;
            state = next(state, c);
            if (state.equals(QE)) {
                return AutomatonResult.rejected(input, QE, i, errorMessage(previous, c));
            }
        }

        // La cadena terminó: solo q2 y q4 son de aceptación.
        if (state.equals(Q1)) {
            return AutomatonResult.rejected(input, state, input.length(),
                    "Faltan dígitos después del signo '-'");
        }
        if (state.equals(Q3)) {
            return AutomatonResult.rejected(input, state, input.length(),
                    "Faltan dígitos después del punto decimal (5. no es válido)");
        }
        if (input.length() > MAX_LENGTH) {
            return AutomatonResult.rejected(input, state, MAX_LENGTH,
                    "El número excede " + MAX_LENGTH + " caracteres");
        }
        return AutomatonResult.accepted(input, state);
    }

    /** Función de transición δ(estado, carácter). */
    private String next(String state, char c) {
        switch (state) {
            case Q0:
                if (c == '-') return Q1;
                return isDigit(c) ? Q2 : QE;
            case Q1:
                return isDigit(c) ? Q2 : QE;
            case Q2:
                if (isDigit(c)) return Q2;
                return c == '.' ? Q3 : QE;
            case Q3:
                return isDigit(c) ? Q4 : QE;
            case Q4:
                return isDigit(c) ? Q4 : QE;
            default:
                return QE;
        }
    }

    /** Mensaje según el estado donde falló la transición. */
    private String errorMessage(String state, char c) {
        if (state.equals(Q0)) {
            return c == '.'
                    ? "Un número no puede comenzar con punto (.5 no es válido)"
                    : "Carácter no permitido en número: '" + c + "'";
        }
        if (state.equals(Q1)) {
            return "Faltan dígitos después del signo '-'";
        }
        if (state.equals(Q3)) {
            return "Faltan dígitos después del punto decimal";
        }
        if (c == '-') {
            return "El signo '-' solo puede ir al inicio del número";
        }
        if (c == '.') {
            return "Un número solo puede tener un punto decimal";
        }
        return "Carácter no permitido en número: '" + c + "'";
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
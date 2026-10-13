package model.automata;

/** Resultado de un reconocimiento: aceptada o rechazada, con el motivo. */
public class AutomatonResult {

    private final boolean accepted;
    private final String input;
    private final String finalState;
    private final int errorIndex;     // posición del carácter que falló (0 si fue aceptada)
    private final String message;

    private AutomatonResult(boolean accepted, String input, String finalState,
                            int errorIndex, String message) {
        this.accepted = accepted;
        this.input = input;
        this.finalState = finalState;
        this.errorIndex = errorIndex;
        this.message = message;
    }

    public static AutomatonResult accepted(String input, String finalState) {
        return new AutomatonResult(true, input, finalState, 0, "Cadena aceptada");
    }

    public static AutomatonResult rejected(String input, String finalState,
                                           int errorIndex, String message) {
        return new AutomatonResult(false, input, finalState, errorIndex, message);
    }

    public boolean isAccepted() { return accepted; }
    public String getInput() { return input; }
    public String getFinalState() { return finalState; }
    public int getErrorIndex() { return errorIndex; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return (accepted ? "ACEPTA" : "RECHAZA") + " '" + input + "' [" + finalState + "] " + message;
    }
}
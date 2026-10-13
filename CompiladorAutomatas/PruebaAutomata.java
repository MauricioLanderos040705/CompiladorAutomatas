import model.automata.AutomatonIdentifier;

public class PruebaAutomata {
    public static void main(String[] args) {
        AutomatonIdentifier afd = new AutomatonIdentifier();
        String[] pruebas = {
            "cantidad", "_precio", "total2", "Cantidad", "a", "_",   // válidas
            "2total", "mi-var", "mi var", "año", "", "x".repeat(41)  // inválidas
        };
        for (String p : pruebas) {
            System.out.println(afd.recognize(p));
        }
    }
}
import model.automata.AutomatonNumber;

public class PruebaNumeros {
    public static void main(String[] args) {
        AutomatonNumber afd = new AutomatonNumber();
        String[] pruebas = {
            "0", "25", "-7", "3.14", "-3.5", "007", "0.5",                    // válidas
            "5.", ".5", "-", "1.2.3", "3x", "--5", "5-", "-.5", "", "1".repeat(41) // inválidas
        };
        for (String p : pruebas) {
            System.out.println(afd.recognize(p));
        }
    }
}
import model.automata.AutomatonIdentifier;
import model.automata.AutomatonNumber;
import model.compiler.CompilationResult;
import model.compiler.Compiler;
import model.keywords.KeywordHighlighter;
import model.keywords.ReservedWords;
import model.lexer.Lexer;
import model.lexer.Token;
import model.symboltable.Symbol;
import model.symboltable.SymbolTable;
import model.symboltable.SymbolTableFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.StyleConstants;

/** Pruebas sin librerias externas; un fallo detiene la ejecucion. */
public class PruebaIntegracion {

    private static int comprobaciones;

    public static void main(String[] args) throws Exception {
        probarAutomatas();
        probarTablaEnMemoria();
        probarCompilacionYArchivo();
        probarColoreado();
        System.out.println("Correcto: " + comprobaciones + " comprobaciones");
    }

    private static void verificar(boolean condicion, String descripcion) {
        if (!condicion) {
            throw new AssertionError(descripcion);
        }
        comprobaciones++;
    }

    private static void probarAutomatas() {
        AutomatonIdentifier identificador = new AutomatonIdentifier();
        String[] identificadoresValidos = {"cantidad", "_precio", "total2", "Cantidad", "a", "_", "x".repeat(40)};
        String[] identificadoresInvalidos = {"2total", "mi-var", "mi var", "a\u00f1o", "", "x".repeat(41)};
        for (String cadena : identificadoresValidos) {
            verificar(identificador.recognize(cadena).isAccepted(), "Identificador valido: " + cadena);
        }
        for (String cadena : identificadoresInvalidos) {
            verificar(!identificador.recognize(cadena).isAccepted(), "Identificador invalido: " + cadena);
        }

        AutomatonNumber numero = new AutomatonNumber();
        String[] numerosValidos = {"0", "25", "-7", "3.14", "-3.5", "007", "0.5", "1".repeat(40)};
        String[] numerosInvalidos = {"5.", ".5", "-", "1.2.3", "3x", "--5", "5-", "-.5", "", "1".repeat(41)};
        for (String cadena : numerosValidos) {
            verificar(numero.recognize(cadena).isAccepted(), "Numero valido: " + cadena);
        }
        for (String cadena : numerosInvalidos) {
            verificar(!numero.recognize(cadena).isAccepted(), "Numero invalido: " + cadena);
        }
    }

    private static void probarTablaEnMemoria() {
        SymbolTable tabla = new SymbolTable();
        Symbol cantidad = tabla.add("cantidad", Symbol.Category.IDENTIFIER);
        verificar(tabla.find("cantidad") == cantidad, "Encontrar simbolo por lexema");
        verificar(tabla.contains("cantidad") && !tabla.contains("precio"), "Consultar existencia");
        verificar(tabla.add("cantidad", Symbol.Category.IDENTIFIER) == null, "Excluir duplicados");
        Symbol precio = tabla.add("precio", Symbol.Category.IDENTIFIER);
        verificar(cantidad.getId() == 1 && precio.getId() == 2 && tabla.size() == 2, "Orden secuencial");
        tabla.clear();
        verificar(tabla.size() == 0 && tabla.find("cantidad") == null, "Limpiar tabla");
        verificar(tabla.add("nuevo", Symbol.Category.IDENTIFIER).getId() == 1, "Reiniciar numeracion");
        verificar(ReservedWords.isReserved("tacoBistec"), "Reconocer reservada");
        verificar(!ReservedWords.isReserved("TacoBistec"), "Distinguir mayusculas");
        verificar(!ReservedWords.isReserved("tacoBistec2"), "Comparar palabra completa");
    }

    private static void probarCompilacionYArchivo() throws Exception {
        Path carpeta = Files.createTempDirectory("compilador-prueba-");
        Path archivo = carpeta.resolve("simbolos.dat");
        try {
            Compiler compilador = new Compiler(archivo.toFile());
            compilador.initialize();
            verificar(compilador.getSymbols().size() == 12, "Cargar las 12 reservadas");
            for (int i = 0; i < 12; i++) {
                verificar(compilador.getSymbols().get(i).getLexeme().equals(ReservedWords.getAll().get(i)),
                        "Orden de reservadas: " + i);
            }
            CompilationResult resultado = compilador.compile(
                    "tacoBistec cantidad=25;\ntacoPastor precio=-3.5; cantidad=cantidad-2;");
            verificar(!resultado.hasErrors(), "Compilar codigo lexico valido");
            verificar(resultado.getSymbols().size() == 17, "Guardar simbolos sin duplicados");
            SymbolTableFile almacenamiento = new SymbolTableFile(archivo.toFile());
            verificar(almacenamiento.count() == 17, "Cantidad de registros");
            verificar(Files.size(archivo) == 17 * 92, "Registros de 92 bytes");
            for (int id = 1; id <= 17; id++) {
                Symbol leido = almacenamiento.read(id);
                Symbol esperado = resultado.getSymbols().get(id - 1);
                verificar(leido.getId() == id && leido.getLexeme().equals(esperado.getLexeme())
                        && leido.getCategory() == esperado.getCategory(), "Acceso aleatorio: " + id);
            }
            verificar(almacenamiento.read(0) == null && almacenamiento.read(18) == null,
                    "Registros fuera de rango");
            List<Symbol> todos = almacenamiento.readAll();
            verificar(todos.size() == 17 && todos.get(16).getLexeme().equals("2"), "Lectura secuencial");

            List<Token> tokens = new Lexer().tokenize("x=-3.5; x=x-2;");
            String[] lexemas = {"x", "=", "-3.5", ";", "x", "=", "x", "-", "2", ";"};
            verificar(tokens.size() == lexemas.length, "Separar signo negativo y resta");
            for (int i = 0; i < lexemas.length; i++) {
                verificar(tokens.get(i).getLexeme().equals(lexemas[i]), "Token: " + i);
            }
            CompilationResult invalido = compilador.compile("tacoBistec x=5.;\ny=.5; z=1.2.3; w=3x; @");
            verificar(invalido.getErrors().size() == 5, "Reportar numeros y simbolos invalidos");
            verificar(invalido.getErrors().get(0).getLine() == 1
                    && invalido.getErrors().get(0).getColumn() == 16, "Ubicacion del primer error");
            verificar(invalido.getErrors().get(1).getLine() == 2
                    && invalido.getErrors().get(1).getColumn() == 3, "Ubicacion del segundo error");
            CompilationResult largo = compilador.compile("a".repeat(41) + " " + "1".repeat(41));
            verificar(largo.getErrors().size() == 2 && largo.getSymbols().size() == 12,
                    "Excluir identificadores y numeros demasiado largos");
            CompilationResult vacio = compilador.compile("");
            verificar(vacio.getSymbols().size() == 12 && almacenamiento.count() == 12,
                    "Reiniciar archivo en cada compilacion");
            verificar(resultado.getSymbols().size() == 17, "Conservar el resultado anterior");
        } finally {
            Files.deleteIfExists(archivo);
            Files.deleteIfExists(carpeta);
        }
    }

    private static void probarColoreado() throws Exception {
        JTextPane[] editor = new JTextPane[1];
        String texto = "tacoBistec\tcantidad=25;\n2tacoBistec tacoBistec2\n guacamole(cantidad>=2){\n picoGallo;\n}";
        SwingUtilities.invokeAndWait(() -> {
            editor[0] = new JTextPane();
            new KeywordHighlighter(editor[0]);
            editor[0].setText(texto);
        });
        // El coloreado programado al insertar se ejecuta antes de esta verificacion.
        SwingUtilities.invokeAndWait(() -> {
            verificar(esReservadaColoreada(editor[0], 0), "Colorear al escribir");
            verificar(!esReservadaColoreada(editor[0], texto.indexOf("cantidad")), "No colorear identificadores");
            verificar(!esReservadaColoreada(editor[0], texto.indexOf("2tacoBistec") + 1),
                    "No colorear una reservada dentro de un numero invalido");
            verificar(!esReservadaColoreada(editor[0], texto.indexOf("tacoBistec2")),
                    "No colorear una reservada dentro de un identificador");
            verificar(esReservadaColoreada(editor[0], texto.indexOf("guacamole")), "Colorear en otra linea");
            verificar(esReservadaColoreada(editor[0], texto.indexOf("picoGallo")), "Colorear despues de espacios");
            editor[0].setText("tacoBistec2");
        });
        SwingUtilities.invokeAndWait(() -> {
            verificar(!esReservadaColoreada(editor[0], 0), "Restaurar estilo al editar una reservada");
        });
    }

    private static boolean esReservadaColoreada(JTextPane editor, int posicion) {
        return StyleConstants.getForeground(editor.getStyledDocument()
                .getCharacterElement(posicion).getAttributes()).equals(KeywordHighlighter.KEYWORD_COLOR);
    }
}

package model.symboltable;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Archivo de acceso aleatorio "Tabla de símbolos".
 * Registros de longitud fija: id(4) + longitud(4) + lexema(40 chars = 80) + categoría(4) = 92 bytes.
 * Solo se ocupa del archivo; la tabla en memoria es SymbolTable.
 */
public class SymbolTableFile {

    public static final int MAX_LEXEME = 40;
    private static final int RECORD_SIZE = 4 + 4 + MAX_LEXEME * 2 + 4;

    private final File file;

    /** Crea el archivo (y su carpeta) si no existen. */
    public SymbolTableFile(File file) throws IOException {
        this.file = file;
        File parent = file.getAbsoluteFile().getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            // Abrir en modo "rw" crea el archivo si no existe.
        }
    }

    /** Vacía el archivo. */
    public void reset() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);
        }
    }

    /** Escribe un registro al final del archivo. */
    public void append(Symbol symbol) throws IOException {
        String lexeme = symbol.getLexeme();
        if (lexeme.length() > MAX_LEXEME) {
            throw new IllegalArgumentException(
                    "El lexema excede " + MAX_LEXEME + " caracteres: " + lexeme);
        }
        ByteBuffer buf = ByteBuffer.allocate(RECORD_SIZE);
        buf.putInt(symbol.getId());
        buf.putInt(lexeme.length());
        for (int i = 0; i < MAX_LEXEME; i++) {
            buf.putChar(i < lexeme.length() ? lexeme.charAt(i) : ' ');
        }
        buf.putInt(symbol.getCategory().ordinal());

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.seek(raf.length());
            raf.write(buf.array());
        }
    }

    /** Lee el registro número id (desde 1) saltando directamente a su posición. */
    public Symbol read(int id) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long position = (long) (id - 1) * RECORD_SIZE;
            if (id < 1 || position + RECORD_SIZE > raf.length()) {
                return null;
            }
            raf.seek(position);
            return readRecord(raf);
        }
    }

    /** Lee todos los registros en orden. */
    public List<Symbol> readAll() throws IOException {
        List<Symbol> list = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long count = raf.length() / RECORD_SIZE;
            for (long i = 0; i < count; i++) {
                list.add(readRecord(raf));
            }
        }
        return list;
    }

    /** Cantidad de registros guardados. */
    public int count() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            return (int) (raf.length() / RECORD_SIZE);
        }
    }

    private Symbol readRecord(RandomAccessFile raf) throws IOException {
        int id = raf.readInt();
        int length = raf.readInt();
        char[] chars = new char[MAX_LEXEME];
        for (int i = 0; i < MAX_LEXEME; i++) {
            chars[i] = raf.readChar();
        }
        int categoryOrdinal = raf.readInt();
        return new Symbol(id, new String(chars, 0, length),
                Symbol.Category.values()[categoryOrdinal]);
    }
}
package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Operaciones de lectura de archivos de texto.
 * No conoce la interfaz gráfica.
 */
public class FileController {

    private static final Charset WINDOWS_1252 = Charset.forName("windows-1252");

    /**
     * Lee un archivo de texto completo.
     * Intenta UTF-8; si el archivo no es UTF-8 válido, usa Windows-1252.
     * Los saltos de línea se normalizan a "\n", como los maneja el editor.
     *
     * @throws IOException si el archivo no se puede leer
     */
    public String readTextFile(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        String text;
        try {
            text = StandardCharsets.UTF_8.newDecoder().decode(
                    java.nio.ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) {
            text = new String(bytes, WINDOWS_1252);
        }
        // Quita el BOM de UTF-8 si lo trae.
        if (!text.isEmpty() && text.charAt(0) == '\uFEFF') {
            text = text.substring(1);
        }
        return text.replace("\r\n", "\n").replace("\r", "\n");
    }
}
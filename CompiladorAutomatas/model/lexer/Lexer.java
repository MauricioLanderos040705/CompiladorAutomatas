package model.lexer;

import model.keywords.ReservedWords;

import java.util.ArrayList;
import java.util.List;

/**
 * Separa el código fuente en tokens.
 * Ignora espacios, tabulaciones y saltos de línea, pero lleva la cuenta
 * de línea y columna. No lanza excepciones: lo no reconocido se devuelve
 * como token UNKNOWN para que se reporte como error más adelante.
 */
public class Lexer {

    // Provisional: se ajusta cuando el equipo defina el lenguaje.
    private static final String[] TWO_CHAR_OPERATORS = {"==", "!=", "<=", ">=", "&&", "||"};
    private static final String ONE_CHAR_OPERATORS = "+-*/%=<>!";
    private static final String DELIMITERS = "(){}[];,";

    public List<Token> tokenize(String source) {
        List<Token> tokens = new ArrayList<>();
        int n = source.length();
        int i = 0;
        int line = 1;
        int col = 1;

        while (i < n) {
            char c = source.charAt(i);

            // Espacios en blanco
            if (c == '\n') { line++; col = 1; i++; continue; }
            if (c == ' ' || c == '\t' || c == '\r') { col++; i++; continue; }

            int start = i;
            char next = (i + 1 < n) ? source.charAt(i + 1) : '\0';
            TokenType type;

            // Un número empieza con dígito, con '.' + dígito (.5, que el AFD rechazará)
            // o con '-' + dígito cuando el '-' no puede ser una resta.
            boolean startsNumber = isDigit(c)
                    || (c == '.' && isDigit(next))
                    || (c == '-' && isDigit(next) && allowsNegative(tokens));

            if (isLetter(c)) {
                // Palabra: letras, dígitos y guion bajo
                while (i < n && isWordPart(source.charAt(i))) i++;
                String text = source.substring(start, i);
                type = ReservedWords.isReserved(text)
                        ? TokenType.RESERVED_WORD : TokenType.IDENTIFIER;
            } else if (startsNumber) {
                // Cadena candidata a número (el AFD del punto 8 la validará)
                i++;
                while (i < n && (isWordPart(source.charAt(i)) || source.charAt(i) == '.')) i++;
                type = TokenType.NUMBER;
            } else if (isTwoCharOperator(source, i)) {
                i += 2;
                type = TokenType.OPERATOR;
            } else if (ONE_CHAR_OPERATORS.indexOf(c) >= 0) {
                i++;
                type = TokenType.OPERATOR;
            } else if (DELIMITERS.indexOf(c) >= 0) {
                i++;
                type = TokenType.DELIMITER;
            } else {
                i++;
                type = TokenType.UNKNOWN;
            }

            String lexeme = source.substring(start, i);
            tokens.add(new Token(type, lexeme, line, col));
            col += i - start;
        }
        return tokens;
    }

    /**
     * Un '-' pegado a un dígito es signo (y no resta) solo si el token anterior
     * no es un valor: identificador, número, ')' o ']'.
     */
    private boolean allowsNegative(List<Token> tokens) {
        if (tokens.isEmpty()) return true;
        Token last = tokens.get(tokens.size() - 1);
        if (last.getType() == TokenType.IDENTIFIER || last.getType() == TokenType.NUMBER) {
            return false;
        }
        if (last.getType() == TokenType.DELIMITER
                && (last.getLexeme().equals(")") || last.getLexeme().equals("]"))) {
            return false;
        }
        return true;
    }

    private boolean isTwoCharOperator(String s, int i) {
        if (i + 1 >= s.length()) return false;
        String pair = s.substring(i, i + 2);
        for (String op : TWO_CHAR_OPERATORS) {
            if (op.equals(pair)) return true;
        }
        return false;
    }

    private boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isWordPart(char c) {
        return isLetter(c) || isDigit(c);
    }
}
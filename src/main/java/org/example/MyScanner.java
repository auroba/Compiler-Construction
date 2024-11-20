package org.example;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class MyScanner {
    enum TOKEN {
        SCANEOF, ID, INTLITERAL, INTDATATYPE, DECLARE,
        PRINT, SET, EQUALS, IF, THEN, ENDIF, CALC, PLUS
    } //enumeration of token types

    private List<String> reservedWords = new ArrayList<>(); //List to store reserved words
    private PushbackReader input; //PushbackReader for input
    private StringBuilder buffer = new StringBuilder(); //StringBuilder for the token buffer to store lexemes

    /**
     * One parameter constructor to initialize pushbackReader and reserved words
     * @param input
     */
    public MyScanner(PushbackReader input) {
        this.input = input;
        reservedWords.add("declare");
        reservedWords.add("int");
        reservedWords.add("print");
        reservedWords.add("set");
        reservedWords.add("if");
        reservedWords.add("then");
        reservedWords.add("endif");
        reservedWords.add("calc");
    }

    /**
     * Reads the next character from the input
     * @return the next character
     */
    private int readNextChar() {
        int c;
        try {
            c = input.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    /**
     * Unread the last character from the input
     * @param c
     */
    private void unread(int c) {
        try {
            input.unread(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if there is a whitespace in the input
     * @param c
     * @return true if there is a whitespace
     */
    private boolean isWhiteSpace(int c) {
        //checks for space, tab, carriage return, line feed
        if ((c == 32) || (c == 9) || (c == 13) || (c == 10)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the content of the buffer as a string
     * @return the buffer token string
     */
    String getTokenBufferString() {
        return buffer.toString();
    }

    /**
     * Method to scan input and return the TOKEN enum value corresponding to it
     * @return TOKEN enum value
     * @throws InputMismatchException
     */
    public TOKEN scan() throws InputMismatchException {
        int c;
        buffer.setLength(0); // clear buffer
        c = readNextChar(); // read the first character
       // buffer.setLength(0); // clear buffer

        if (c == -1) {
            return TOKEN.SCANEOF; // if there is an empty program return SCANEOF
        }
        // while the scanner has not reached the end of the file
        while (c != -1) {
            if (isWhiteSpace(c)) {
                c = readNextChar(); // ignore whitespaces
                continue;
            }
            if (c == '=') {
                return TOKEN.EQUALS; // if input character is = return EQUALS
            } else if (c == '+') {
                return TOKEN.PLUS; // if input character is + return PLUS
            }
            // if the character is a digit
            if (Character.isDigit(c)) {
                buffer.setLength(0); // clear buffer
                buffer.append((char) c); // add character to buffer
                c = readNextChar(); // read next character
                while (Character.isDigit(c)) {
                    buffer.append((char) c); // while the character is a digit add it to the buffer
                    c = readNextChar(); // read next character
                }
                unread(c); // unread the last character
                return TOKEN.INTLITERAL; // return INTLITERAL
            }
            // while the character is a letter
            while (Character.isLetter(c)) {
                buffer.append((char) c); // add character to buffer
                c = readNextChar(); // read next character
            }

            String lexeme = getTokenBufferString(); // set lexeme as the token buffer string
            // check if the lexeme is a reserved word and return the matching token
            if (reservedWords.contains(lexeme)) {
                if (lexeme.equals("declare")) {
                    return TOKEN.DECLARE;
                } else if (lexeme.equals("int")) {
                    return TOKEN.INTDATATYPE;
                } else if (lexeme.equals("print")) {
                    return TOKEN.PRINT;
                } else if (lexeme.equals("set")) {
                    return TOKEN.SET;
                } else if (lexeme.equals("if")) {
                    return TOKEN.IF;
                } else if (lexeme.equals("then")) {
                    return TOKEN.THEN;
                } else if (lexeme.equals("endif")) {
                    return TOKEN.ENDIF;
                } else if (lexeme.equals("calc")) {
                    return TOKEN.CALC;
                }
            } else {
                return TOKEN.ID; // if it is not a reserved word it is an ID
            }
        }
        throw new InputMismatchException("No match found"); // throw exception if no token matches
    }
}


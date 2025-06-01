package com.rpal.lex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LexicalAnalyzer {
    private ArrayList<Token> tokenList; // This list stores tokens created during lexical analysis for later parsing.
    private BufferedReader reader;
    // RPAL language's reserved keywords
    private List<String> keywords = Arrays.asList("let", "in", "fn", "where", "aug", "or", "not", "gr", "ge", "ls",
            "le", "eq", "ne",
            "true", "false", "nil", "dummy", "within", "and", "rec", "stem", "stern", "conc");

    // Regular expressions defining fundamental lexical components
    private String letter = "[A-Za-z]";
    private String digit = "[0-9]";
    private String operator_symbol = "[+|\\-|*|<|>|&|.|@|/|:|=|~|\\||$|!|#|%|`|_|\\[|\\]|{|}|\\\"|?|^|'|]";

    private boolean readerClosed = false; // Ensures the reader doesn’t continue once the file ends

    public LexicalAnalyzer(File file) {
        this.tokenList = new ArrayList<Token>();
        try {
            this.reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("File is not found");
        }

        String nextChr = null; // Holds the character just read
        String buffer = null; // Temporarily keeps characters that were read too early

        while ((nextChr = nextChr()) != null) { // Continues until the end of the file
            constructToken(nextChr, buffer);
            if (readerClosed) {
                break;
            }
        }

        // Append a special EOF token to mark the end
        Token EofToken = new Token();
        EofToken.setType("EOF");
        EofToken.setValue("EOF");
        tokenList.add(EofToken);

        // Post-process the tokens to remove unwanted ones and modify specific types
        screening();
    }

    /**
     * Returns the full list of tokens generated from input
     * 
     * @return ArrayList<Token>
     */
    public ArrayList<Token> getTokenList() {
        return tokenList;
    }

    /**
     * Interprets characters and creates a new token accordingly
     * 
     * @param nextChr the current character to examine
     * @param buffer  holds any character that was read but not yet processed
     */
    private void constructToken(String nextChr, String buffer) {
        Token token = new Token();
        String value = "";

        if (nextChr.matches(letter)) { // Starts an identifier if the first character is a letter
            value = nextChr;
            while ((nextChr = nextChr()) != null) {
                // Keeps building the identifier using letters, digits, or underscore
                if (nextChr.matches("[" + letter + "|" + digit + "|'_']*")) {
                    value += nextChr;
                } else {
                    // Save this character for the next token
                    buffer = nextChr;
                    break;
                }
            }

            token.setType("IDENTIFIER");
            token.setValue(value);
            tokenList.add(token);

        } else if (nextChr.matches(digit)) { // Detects and constructs an integer token
            value = nextChr;
            while ((nextChr = nextChr()) != null) {
                // Gathers all following digits
                if (nextChr.matches("[" + digit + "]*")) {
                    value += nextChr;
                } else {
                    buffer = nextChr;
                    break;
                }
            }
            token.setType("INTEGER");
            token.setValue(value);
            tokenList.add(token);

        } else if (nextChr.matches("[\']")) { // Handles the start of a string literal
            value = nextChr;
            String prevChr = nextChr;
            while ((nextChr = nextChr()) != null) {
                // Ends the string when it hits an unescaped closing quote
                if (!(prevChr.equals("\\")) && nextChr.matches("[\']")) {
                    value += nextChr;
                    token.setType("STRING");
                    token.setValue(value);
                    tokenList.add(token);
                    break;
                } else if (nextChr.matches("[\\t|\\n|\\\\|\\\'|'('|')'|';'|','|' '|" + letter + "|" + digit + "|"
                        + operator_symbol + "]*")) { // Accepts various characters inside a string
                    prevChr = nextChr;
                    value += nextChr;
                } else if (prevChr.equals("\\") && nextChr.matches("[\']")) { // Deals with escaped single quote
                    prevChr = nextChr;
                    value += nextChr;
                } else {
                    buffer = nextChr;
                    break;
                }
            }

        } else if (nextChr.matches(operator_symbol)) { // Recognizes operators or comments
            value = nextChr;
            String prevChr = nextChr;
            boolean isComment = false;
            while ((nextChr = nextChr()) != null) {
                // Begins a comment if two slashes are found
                if (prevChr.matches("[/]") && nextChr.matches("[/]")) {
                    // Separate the part before comment as a standalone operator
                    if (value.length() > 1) {
                        String tokenValue = value.substring(0, value.length() - 1);
                        token.setType("OPERATOR");
                        token.setValue(tokenValue);
                        tokenList.add(token);
                    }
                    token = new Token();
                    isComment = true;
                    value = "//";
                    while ((nextChr = nextChr()) != null) {
                        // Collects all characters in the comment
                        if (nextChr.matches("['\''|'('|')'|';'|','|' '|'\\t'|" + letter + "|" + digit + "|"
                                + operator_symbol + "]*")) {
                            value += nextChr;
                        } else if (nextChr.matches("[\\n]")) { // Stops at newline
                            value += nextChr;
                            token.setType("DELETE");
                            token.setValue(value);
                            tokenList.add(token);
                            break;
                        }
                    }
                } else if (nextChr.matches("[" + operator_symbol + "]*")) { // Builds a multi-symbol operator
                    value += nextChr;
                    prevChr = nextChr;
                } else {
                    buffer = nextChr;
                    token.setType("OPERATOR");
                    token.setValue(value);
                    tokenList.add(token);
                    break;
                }
                if (isComment) {
                    break; // Stop reading after a comment is captured
                }
            }

        } else if (nextChr.matches("[\\s|\\t|\\n]")) { // Handles whitespace tokens
            value = nextChr;
            while ((nextChr = nextChr()) != null) {
                // Collects all spaces, tabs, and line breaks
                if (nextChr.matches("[\\s\\t\\n]*")) {
                    value += nextChr;
                } else {
                    buffer = nextChr;
                    break;
                }
            }
            token.setType("DELETE");
            token.setValue(value);
            tokenList.add(token);

        } else if (nextChr.matches("[(]")) { // Opening parenthesis detected
            value = nextChr;
            token.setType("L_PAREN");
            token.setValue(value);
            tokenList.add(token);

        } else if (nextChr.matches("[)]")) { // Closing parenthesis found
            value = nextChr;
            token.setType("R_PAREN");
            token.setValue(value);
            tokenList.add(token);

        } else if (nextChr.matches("[;]")) { // Semicolon punctuation
            value = nextChr;
            token.setType("SEMICOLON");
            token.setValue(value);
            tokenList.add(token);

        } else if (nextChr.matches("[,]")) { // Comma punctuation
            value = nextChr;
            token.setType("COMMA");
            token.setValue(value);
            tokenList.add(token);

        }

        // If we saved a character earlier, use it now to avoid missing any input
        if (buffer != null) {
            nextChr = buffer;
            buffer = null;
            constructToken(nextChr, buffer);
        }
    }

    /**
     * Reads one character from the input file and converts it to a string
     * 
     * @return the next character or null if at end of file
     */
    private String nextChr() {
        String nextChr = null;
        try {
            if (readerClosed) {
                return null; // Do not read if the file stream is already shut
            }
            int chr = reader.read(); // Fetch the next byte from the input
            if (chr != -1) { // If not EOF
                nextChr = Character.toString((char) chr);
            } else {
                readerClosed = true; // Prevent further reads if finished
                reader.close(); // Close the file stream
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        return nextChr;
    }

    /**
     * Cleans and adjusts the token list after initial scanning.
     * - Removes all tokens marked DELETE (e.g., whitespace and comments).
     * - Converts identifiers to keywords when applicable.
     */
    private void screening() {
        // Iterator used to safely modify list while looping
        for (Iterator<Token> it = tokenList.iterator(); it.hasNext();) {
            Token tok = it.next();

            // Remove all tokens classified as DELETE
            if ("DELETE".equals(tok.getType())) {
                it.remove();
            }

            // If a token is an identifier and matches a reserved word, update its type
            if ("IDENTIFIER".equals(tok.getType())
                    && keywords.contains(tok.getValue())) {
                tok.setType("KEYWORD");
            }
        }
    }

}

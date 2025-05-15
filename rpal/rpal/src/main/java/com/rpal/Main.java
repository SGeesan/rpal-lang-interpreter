package com.rpal;

import java.io.File;
import java.util.List;

import com.rpal.lex.LexicalAnalyzer;
import com.rpal.lex.Token;
import com.rpal.parser.AST;
import com.rpal.parser.Parser;

public class Main {
    public static void main(String[] args) {
        // Check if a filename is given
        if (args.length < 1) {
            System.out.println("Usage: java com.rpal.Main <sourcefile.rpal>");
            return;
        }
        // Generate the lexical analyser targeting given file
        File file = new File(args[0]);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(file);
        // Tokanize the input
        List<Token> tokenList = lexicalAnalyzer.getTokenList();
        // Build AST
        Parser parser = new Parser(tokenList);
        AST tree = parser.buildAst();
        // Standardise AST
        tree.standardize();
    }
}
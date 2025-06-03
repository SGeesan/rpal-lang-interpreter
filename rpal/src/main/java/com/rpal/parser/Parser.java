package com.rpal.parser;

import java.util.List;
import java.util.Stack;

import com.rpal.lex.Token;

public class Parser {
    List<Token> tokens;
    private Stack<ASTNode> stack;

    public Parser(List<Token> tokenList) {
        this.tokens = tokenList;
        this.stack = new Stack<ASTNode>();
    }

    public AST buildAst() {
        E();
        return (new AST(stack.pop()));
    }

    // Support methods:
    private Token peek() {
        /* peek at the first token on the list */
        if (tokens.size() == 0) {
            throw new RuntimeException("Tokenizer error. EoF not found");
        }
        return tokens.get(0);
    }

    private boolean ValueIn(Token token, String... values) {
        // check if token's value is one of the expected values
        for (String val : values) {
            if (token.getValue().equals(val)) {
                return true;
            }
        }
        return false;
    }

    private void ensureValueIn(Token token, String... values) {
        // ensure token's value matches one of the expected values, else throw

        if (!ValueIn(token, values)) {
            throw new ParserException("Expected :" + String.join(" / ", values) +
                    " but found \"" + token.getValue() + "\"");
        }
    }

    private boolean TypeIn(Token token, String... types) {
        // check if token's type is one of the expected types
        for (String type : types) {
            if (token.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private void ensureTypeIn(Token token, String... types) {
        // ensure token's type matches one of the expected types, else throw
        if (!TypeIn(token, types)) {
            throw new ParserException("Expected type: " + String.join(" / ", types) +
                    " but found \"" + token.getType() + "\"");
        }
    }

    private void buildTree(String type, int n) {
        /* builds bottom up trees and adds to the stack */
        ASTNode p = null;
        // collect the siblings
        for (int i = 0; i < n; i++) {
            ASTNode c = stack.pop();
            c.setRight(p);
            p = c;
        }
        // assign to the new root
        ASTNode newNode = new ASTNode(type);
        newNode.setLeft(p);
        newNode.setRight(null);
        stack.push(newNode);

    }

    // Recursive Parser processes
    private void E() {
        Token next = peek();
        if (next.getValue().equals("let")) {
            // E -> 'let' D in E => ’let’
            tokens.remove(0);
            D();
            ensureValueIn(peek(), "in");
            tokens.remove(0);
            E();
            buildTree("let", 2);
        } else if (next.getValue().equals("fn")) {
            // E -> 'fn' Vb+ '.' E => ’lambda’
        } else if (TypeIn(next, "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(next, "true", "false", "nil", "(", "dummy", "+", "-", "not")) {
            // E -> Ew
            EW();
        } else {
            throw new ParserException("Parse failed at: " + next.getValue());
        }
    }

    private void EW() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'EW'");
    }

    private void D() {
        throw new UnsupportedOperationException("Unimplemented method 'D'");
    }

}

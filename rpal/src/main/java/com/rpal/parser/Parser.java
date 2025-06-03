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
            throw new RuntimeException("Tokenizer error. EOF not found");
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

    public ASTNode rightAsTree(ASTNode root, int operators){
        /*
         * Converts a given binary tree into right associative
         */
        if (operators>1) {
            ASTNode leftResult = rightAsTree(root.getLeft(), operators-1);
            ASTNode rightEnd = root.getLeft();
            rightEnd.getLeft().getRight().setRight(root.getRight());
            root.setLeft(rightEnd.getLeft().getRight());
            leftResult.getLeft().setRight(root);
            return leftResult;
        } else {
            return root;
        }
    }
    // Recursive Parser processes:
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
            tokens.remove(0);
            Vb();
            int N = 1;
            while (TypeIn(peek(), "IDENTIFIER") || ValueIn(peek(), "(")) {
                Vb();
                N++;
            }
            ensureValueIn(peek(), ".");
            tokens.remove(0);
            E();
            buildTree("lambda", N + 1);

        } else if (TypeIn(next, "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(next, "true", "false", "nil", "(", "dummy", "+", "-", "not")) {
            // E -> Ew
            Ew();
        } else {
            throw new ParserException("Parse failed at: " + next.getValue());
        }
    }

    private void Ew() {
        Token next = peek();
        if (TypeIn(next, "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(next, "true", "false", "nil", "(", "dummy", "+", "-", "not")) {
            T();
            if (ValueIn(peek(), "where")) {
                // Ew -> T where Dr => where
                tokens.remove(0);
                Dr();
                buildTree("where", 2);
            } else {
                // Ew -> T
                ensureValueIn(peek(), "EOF", ")", "and", "within", "in");
            }
        } else {
            throw new ParserException("Parse failed at: " + next.getValue());
        }
    }

    private void T() {
        Ta();
        if (ValueIn(peek(), ",")) {
            // T -> Ta (, Ta)+
            tokens.remove(0);
            Ta();
            int N = 2;
            while (ValueIn(peek(), ",")) {
                tokens.remove(0);
                Ta();
                N++;
            }
            buildTree("tau", N);
            ensureValueIn(peek(), "where", "EOF", ")", "and", "within", "in");
        }
        // else: T -> Ta

    }

    private void Ta() {
        if (TypeIn(peek(), "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(peek(), "true", "false", "nil", "(", "dummy", "+", "-", "not")) {
            Tc();
            if (ValueIn(peek(), "aug")) {
                // Ta -> Ta aug Tc
                tokens.remove(0);
                Tc();
                buildTree("aug", 2);
                while (ValueIn(peek(), "aug")) {
                    tokens.remove(0);
                    Tc();
                    buildTree("aug", 2);
                }
            }
            // Ta -> Tc
            ensureValueIn(peek(), ",", "where", "EOF", ")", "and", "within", "in");

        } else {
            throw new ParserException("Parse failed at: " + peek().getValue());
        }
    }

    private void Tc() {
        if (TypeIn(peek(), "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(peek(), "true", "false", "nil", "(", "dummy", "+", "-", "not")) {
            B();
            if (ValueIn(peek(), "->")) {
                // Tc -> B -> Tc ’|’ Tc
                tokens.remove(0);
                Tc();
                ensureValueIn(peek(), "|");
                tokens.remove(0);
                Tc();
            } else {
                // Tc -> B
                ensureValueIn(peek(), "|", "aug", ",", "where", "EoF", ")", "and", "within", "in");
            }
        } else {
            throw new ParserException("Parse failed at: " + peek().getValue());
        }
    }

    private void B() {
        if (TypeIn(peek(), "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(peek(), "true", "false", "nil", "(", "dummy", "+", "-", "not")) {
            Bt();
            if (ValueIn(peek(), "or")) {
                // B -> B or Bt
                tokens.remove(0);
                Bt();
                buildTree("or", 2);
                while (ValueIn(peek(), "or")) {
                    tokens.remove(0);
                    Bt();
                    buildTree("or", 2);
                }
            }
            // B -> Bt
            ensureValueIn(peek(), "->", "|", "aug", ",", "where", "EoF", ")", "and", "within", "in");

        } else {
            throw new ParserException("Parse failed at: " + peek().getValue());
        }
    }

    private void Bt() {
        if (TypeIn(peek(), "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(peek(), "true", "false", "nil", "(", "dummy", "+", "-", "not")) {
            Bs();
            if (ValueIn(peek(), "&")) {
                // Bt -> Bt & Bs
                tokens.remove(0);
                Bs();
                buildTree("&", 2);
                while (ValueIn(peek(), "&")) {
                    tokens.remove(0);
                    Bs();
                    buildTree("&", 2);
                }
            }
            // Bt -> Bs
            ensureValueIn(peek(), "or", "->", "|", "aug", ",", "where", "EoF", ")", "and", "within", "in");

        } else {
            throw new ParserException("Parse failed at: " + peek().getValue());
        }

    }

    private void Bs() {
        if (ValueIn(peek(), "not")) {
            // Bs -> not Bp
            tokens.remove(0);
            Bp();
            buildTree("not", 1);
        } else {
            // Bs -> Bp
            Bp();
        }
    }

    private void Bp() {
        if (TypeIn(peek(), "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(peek(), "true", "false", "nil", "(", "dummy", "+", "-")) {
            A();
            switch (peek().getValue()) {
                case "gr": // Bp ->A(’gr’ | ’>’ ) A
                case ">":
                    tokens.remove(0);
                    A();
                    buildTree("gr", 2);
                    break;

                case "ge":
                case ">=":// Bp -> A (’ge’ | ’>=’) A
                    tokens.remove(0);
                    A();
                    buildTree("ge", 2);
                    break;

                case "ls":
                case "<":// Bp -> A (’ls’ | ’<’) A
                    tokens.remove(0);
                    A();
                    buildTree("ls", 2);
                    break;

                case "le":
                case "<=":// Bp -> A (’le’ | ’<=’) A
                    tokens.remove(0);
                    A();
                    buildTree("le", 2);
                    break;

                case "eq":// Bp -> A ’eq’ A
                    tokens.remove(0);
                    A();
                    buildTree("eq", 2);
                    break;

                case "ne":// Bp -> A ’ne’ A
                    tokens.remove(0);
                    A();
                    buildTree("ne", 2);
                    break;

                default:
                    // Bp -> A
                    ensureValueIn(peek(), "&", "or", "->", "|", "aug", ",", "where", "EoF", ")", "and", "within", "in");
            }

        } else {
            throw new ParserException("Parse failed at: " + peek().getValue());
        }
    }

    private void A() {
        if (ValueIn(peek(), "-")) {
            // A -> - At => neg
            tokens.remove(0);
            At();
            buildTree("neg", 1);
        } else {
            if (ValueIn(peek(), "+")) {
                // A -> + At
                tokens.remove(0);
            }
            At();
        }
        while (ValueIn(peek(), "+", "-")) {
            if (ValueIn(peek(), "+")) {// A -> A + At
                tokens.remove(0);
                At();
                buildTree("+", 2);
            } else { // A -> A - At
                tokens.remove(0);
                At();
                buildTree("-", 2);
            }
        }
        ensureValueIn(peek(), "gr", ">", "ge", ">=", "ls", "<", "le", "<=", "eq", "ne", "&", "or", "->", "|", "aug",
                ",", "where", "EOF", ")", "and", "within", "in");
    }

    private void At() {
        if (TypeIn(peek(), "IDENTIFIER", "INTEGER", "STRING") ||
                ValueIn(peek(), "true", "false", "nil", "(", "dummy")) {
            Af();
            while (ValueIn(peek(), "*", "/")) {
                if (ValueIn(peek(), "*")) {// At -> At * Af
                    tokens.remove(0);
                    Af();
                    buildTree("*", 2);
                } else { // At -> At / Af
                    tokens.remove(0);
                    Af();
                    buildTree("/", 2);
                }
            }
            ensureValueIn(peek(), "+", "-", "gr", ">", "ge", ">=", "ls", "<", "le", "<=", "eq", "ne", "&", "or", "->", "|", "aug", ",", "where", "EOF", ")", "and", "within", "in");
        }
        else{
            throw new ParserException("Parse failed at: " + peek().getValue());
        }
    }

    private void Af() {
        Ap();
    }

    private void Ap() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'A'");
    }

    private void R() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'A'");
    }

    private void Rn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'A'");
    }

    private void D() {
        throw new UnsupportedOperationException("Unimplemented method 'D'");
    }

    private void Dr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Dr'");
    }

    private void Vb() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Vb'");
    }
}

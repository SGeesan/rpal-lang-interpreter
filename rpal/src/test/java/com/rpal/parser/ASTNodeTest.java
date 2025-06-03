package com.rpal.parser;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class ASTNodeTest {
    @Test
    public void testPrintTree() {
        // Build tree:
        //       A
        //      / \
        //     B   C
        //    /
        //   D

        ASTNode root = new ASTNode("A");
        ASTNode b = new ASTNode("B");
        ASTNode c = new ASTNode("C");
        ASTNode d = new ASTNode("D");

        root.setLeft(b);
        b.setLeft(d);
        b.setRight(c);

        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Run print
        root.printTree();

        // Restore System.out
        System.setOut(originalOut);

        String actual = outContent.toString().replace("\r\n", "\n").trim();
        String expected = ("A\n" +
                           ". B\n" +
                           ". . D\n" +
                           ". C").trim();
        
        assertEquals(expected, actual);
    }

    @Test
public void testDeepLeftTree() {
    // Tree:
    //    X
    //    |
    //    Y
    //    |
    //    Z

    ASTNode root = new ASTNode("X");
    ASTNode y = new ASTNode("Y");
    ASTNode z = new ASTNode("Z");

    root.setLeft(y);
    y.setLeft(z);

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    root.printTree();

    System.setOut(originalOut);

    String actual = outContent.toString().replace("\r\n", "\n").trim();
    String expected = ("X\n" +
                       ". Y\n" +
                       ". . Z").trim();

    assertEquals(expected, actual);
}

@Test
public void testWideSiblingLine() {
    // Tree:
    //    P
    //     \
    //      Q
    //       \
    //        R

    ASTNode root = new ASTNode("P");
    ASTNode q = new ASTNode("Q");
    ASTNode r = new ASTNode("R");

    root.setRight(q);
    q.setRight(r);

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    root.printTree();

    System.setOut(originalOut);

    String actual = outContent.toString().replace("\r\n", "\n").trim();
    String expected = ("P\n" +
                       "Q\n" +
                       "R").trim();

    assertEquals(expected, actual);
}

}


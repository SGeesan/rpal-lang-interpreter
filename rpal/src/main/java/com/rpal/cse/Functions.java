package com.rpal.cse;
import java.util.ArrayList;
import java.util.Collections;



/*
 * This class provides implementations for RPAL built-in functions
 */
public class Functions {

    /*
     * Checks if a given identifier corresponds to an RPAL built-in function.
     */
    public static boolean checkInBuilt(String name) {
        ArrayList<String> functionNames = new ArrayList<String>();

        Collections.addAll(functionNames, "Print","Stem","Stern","Conc","Order","Null",
                "Isinteger","Istruthvalue","Isstring","Istuple","Isfunction","Isdummy","ItoS");

        return functionNames.contains(name);
    }

    /*
     * Prints a value based on its type (integer, string, tuple, etc.)
     */
    public static void Print(CSNode node) {
        switch (node.getType()) {

            // Handle direct values like integers, strings, truth values, and nil
            case "INTEGER":
            case "STRING":
            case "TRUTHVALUE":
            case "NIL":
                // Replace escape sequences for newline and tab
                if (node.getName().contains("\\n")) {
                    node.setName(node.getName().replace("\\n", "\n"));
                }
                if(node.getName().contains("\\t")) {
                    node.setName(node.getName().replace("\\t", "\t"));
                }
                System.out.print(node.getName());
                break;

            // Handle tuple printing
            case "tau":
            case "tuple":
                System.out.print("(");
                for (int i = 0; i < node.getTuple().size(); i++) {
                    System.out.print(node.getTuple().get(i).getName());
                    if (i != node.getTuple().size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print(")");
                break;

            // Handle lambda closure printing
            case "lambdaClosure":
                System.out.print("[lambda closure: ");
                for (int i = 0; i < node.getLambdavar().size(); i++) {
                    System.out.print(node.getLambdavar().get(i));
                    // TODO: Handle formatting for multiple variables if needed
                }
                System.out.print(": ");
                System.out.print(node.getLambdano());
                System.out.print("]");
                break;

            default:
                System.out.println(); // Default behavior: print a newline
                break;
        }
    }

    /*
     * Returns the first character of a string node.
     */
    public static CSNode Stem(CSNode node) {
        if (node.getType().equals("STRING")) {
            CSNode newNode = node.duplicate();
            newNode.setName(newNode.getName().substring(0,1));
            return newNode;
        } else {
            throw new CSE_Exception("Argument is not a string");
        }
    }

    /*
     * Returns the string excluding its first character.
     */
    public static CSNode Stern(CSNode node) {
        if (node.getType().equals("STRING")) {
            CSNode newNode = node.duplicate();
            newNode.setName(newNode.getName().substring(1));
            return newNode;
        } else {
            throw new CSE_Exception("Argument is not a string");
        }
    }

    /*
     * Wraps the first string node for concatenation.
     */
    public static CSNode ConcOne(CSNode node1) {
        if (node1.getType().equals("STRING")) {
            CSNode concOne = new CSNode("IDENTIFIER","ConcOne");
            concOne.getTuple().add(node1);
            return concOne;
        } else {
            throw new CSE_Exception("Argument is not a string");
        }
    }

    /*
     * Concatenates two string nodes.
     */
    public static CSNode Conc(CSNode node1, CSNode node2) {
        if (node2.getType().equals("STRING")) {
            String conc = node1.getName().concat(node2.getName());
            return new CSNode("STRING", conc);
        } else {
            throw new CSE_Exception("Argument is not a string");
        }
    }

    /*
     * Returns the number of elements in a tuple.
     */
    public static CSNode Order(CSNode tupleNode) {
        if (tupleNode.getIsTuple()) {
            int num = tupleNode.getTuple().size();
            return new CSNode("INTEGER", String.valueOf(num));
        } else {
            throw new CSE_Exception("Attempt to find the order of a non-tuple");
        }
    }

    /*
     * Checks if a tuple is empty (NIL).
     */
    public static CSNode Null(CSNode tupleNode) {
        if (tupleNode.getTuple().size() == 0 && tupleNode.getIsTuple()) {
            return new CSNode("TRUTHVALUE", "true");
        } else {
            return new CSNode("TRUTHVALUE", "false");
        }
    }

    /*
     * Type-check: Returns true if node is of type INTEGER.
     */
    public static CSNode Isinteger(CSNode node) {
        if (node.getType().equals("INTEGER")) {
            return new CSNode("TRUTHVALUE", "true");
        } else {
            return new CSNode("TRUTHVALUE", "false");
        }
    }

    /*
     * Type-check: Returns true if node is of type TRUTHVALUE.
     */
    public static CSNode Istruthvalue(CSNode node) {
        if (node.getType().equals("TRUTHVALUE")) {
            return new CSNode("TRUTHVALUE", "true");
        } else {
            return new CSNode("TRUTHVALUE", "false");
        }
    }

    /*
     * Type-check: Returns true if node is of type STRING.
     */
    public static CSNode Isstring(CSNode node) {
        if (node.getType().equals("STRING")) {
            return new CSNode("TRUTHVALUE", "true");
        } else {
            return new CSNode("TRUTHVALUE", "false");
        }
    }

    /*
     * Type-check: Returns true if node is a tuple.
     */
    public static CSNode Istuple(CSNode node) {
        if (node.getIsTuple()) { 
            return new CSNode("TRUTHVALUE", "true");
        } else {
            return new CSNode("TRUTHVALUE", "false");
        }
    }

    /*
     * Type-check: Returns true if node is a function.
     */
    public static CSNode Isfunction(CSNode node) {
        if (node.getType().equals("FUNCTION")) { 
            return new CSNode("TRUTHVALUE", "true");
        } else {
            return new CSNode("TRUTHVALUE", "false");
        }
    }

    /*
     * Type-check: Returns true if node is a dummy type.
     */
    public static CSNode Isdummy(CSNode node) {
        if (node.getType().equals("DUMMY")) {
            return new CSNode("TRUTHVALUE", "true");
        } else {
            return new CSNode("TRUTHVALUE", "false");
        }
    }

    /*
     * Converts an integer node to a string node.
     */
    public static CSNode intToStr(CSNode intNode) {
        if (intNode.getType().equals("INTEGER")) {
            CSNode strNode = intNode.duplicate();
            strNode.setType("STRING");
            return strNode;
        } else {
            throw new CSE_Exception("Argument is not an Integer");
        }
    }

}


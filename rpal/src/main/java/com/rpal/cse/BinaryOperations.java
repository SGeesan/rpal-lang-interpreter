package com.rpal.cse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




/*
 * This class provides static methods for handling binary operations
 * between CSNode elements in the RPAL evaluation process.
 */
public class BinaryOperations {
    
    /*
     * Adds two integer CSNode values and returns the result as a new INTEGER node.
     */
    public static CSNode add(CSNode node1, CSNode node2){
        // Ensure both nodes are of INTEGER type
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            int num1 = Integer.parseInt(node1.getName());
            int num2 = Integer.parseInt(node2.getName());
            int sum = num1 + num2;

            // Return a new node representing the result
            return new CSNode("INTEGER", String.valueOf(sum));
        } else {
            // Throw error if operands are not integers
            throw new CSE_Exception("Operator is not Integer");
        }
    } 

    /*
     * Subtracts the second INTEGER node from the first and returns the result.
     */
    public static CSNode subtract(CSNode node1, CSNode node2) {
        // Ensure both nodes are of INTEGER type
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            int num1 = Integer.parseInt(node1.getName());
            int num2 = Integer.parseInt(node2.getName());
            int diff = num1 - num2;

            // Return result as a new INTEGER node
            return new CSNode("INTEGER", String.valueOf(diff));
        } else {
            // Throw error if operands are not integers
            throw new CSE_Exception("Operator is not Integer");
        }
    }

    /*
     * Multiplies two INTEGER nodes and returns the result.
     */
    public static CSNode multiply(CSNode node1, CSNode node2) {
        // Ensure both nodes are of INTEGER type
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            int num1 = Integer.parseInt(node1.getName());
            int num2 = Integer.parseInt(node2.getName());
            int mult = num1 * num2;

            // Return product as a new INTEGER node
            return new CSNode("INTEGER", String.valueOf(mult));
        } else {
            // Throw error if operands are not integers
            throw new CSE_Exception("Operator is not Integer");
        }
    }

    /*
     * Performs integer (floor) division on two INTEGER nodes and returns the result.
     */
    public static CSNode divide(CSNode node1, CSNode node2) {
        // Ensure both nodes are of INTEGER type
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            int num1 = Integer.parseInt(node1.getName());
            int num2 = Integer.parseInt(node2.getName());
            int div = Math.floorDiv(num1, num2);

            // Return quotient as a new INTEGER node
            return new CSNode("INTEGER", String.valueOf(div));
        } else {
            // Throw error if operands are not integers
            throw new CSE_Exception("Operator is not Integer");
        }
    }

    /*
     * Raises the first INTEGER node to the power of the second and returns the result.
     */
    public static CSNode power(CSNode node1, CSNode node2) {
        // Ensure both nodes are of INTEGER type
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            int num1 = Integer.parseInt(node1.getName());
            int num2 = Integer.parseInt(node2.getName());
            int power = (int) Math.pow(num1, num2);
            
            // Return power result as a new INTEGER node
            return new CSNode("INTEGER", String.valueOf(power));
        } else {
            // Throw error if operands are not integers
            throw new CSE_Exception("Operator is not Integer");
        }
    }


    /*
    * Static method to compare two CSNodes for equality.
    * Applicable only for INTEGER, STRING, and TRUTHVALUE types.
    */
    public static CSNode isEqual(CSNode node1, CSNode node2) {
        
        List<String> acceptableTypes = new ArrayList<String>(); 
        Collections.addAll(acceptableTypes, "INTEGER", "STRING", "TRUTHVALUE");

        // Ensure both nodes are of the same and acceptable type
        if (acceptableTypes.contains(node1.getType()) && node1.getType().equals(node2.getType())) {
            if (node1.getName().equals(node2.getName())) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } else {
            throw new CSE_Exception("Types do not match");
        }
    }

    /*
    * Static method to check inequality between two CSNodes.
    * Valid for INTEGER, STRING, and TRUTHVALUE types.
    */
    public static CSNode isNotEqual(CSNode node1, CSNode node2) {
        
        List<String> acceptableTypes = new ArrayList<String>(); 
        Collections.addAll(acceptableTypes, "INTEGER", "STRING", "TRUTHVALUE");

        // Ensure both nodes are of the same and valid type
        if (acceptableTypes.contains(node1.getType()) && node1.getType().equals(node2.getType())) {
            if (node1.getName().equals(node2.getName())) {
                return new CSNode("TRUTHVALUE", "false");
            } else {
                return new CSNode("TRUTHVALUE", "true");
            }
        } else {
            throw new CSE_Exception("Types do not match");
        }
    }

    /*
    * Static method to check if node1 is less than node2.
    * Applicable only for INTEGER and STRING types.
    */
    public static CSNode isLessThan(CSNode node1, CSNode node2) {
        
        // Handle INTEGER comparison
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            if (Integer.parseInt(node1.getName()) < Integer.parseInt(node2.getName())) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } 

        // Handle lexicographical STRING comparison
        else if (node1.getType().equals("STRING") && node2.getType().equals("STRING")) {
            if (node1.getName().compareTo(node2.getName()) < 0) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } else {
            throw new CSE_Exception("Types do not match");
        }
    }

    /*
    * Static method to check if node1 is greater than node2.
    * Works for INTEGER and STRING types.
    */
    public static CSNode isGreaterThan(CSNode node1, CSNode node2) {
        
        // Handle INTEGER comparison
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            if (Integer.parseInt(node1.getName()) > Integer.parseInt(node2.getName())) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } 

        // Handle lexicographical STRING comparison
        else if (node1.getType().equals("STRING") && node2.getType().equals("STRING")) {
            if (node1.getName().compareTo(node2.getName()) > 0) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } else {
            throw new CSE_Exception("Types do not match");
        }
    }


    /*
    * Static method to check if node1 is less than or equal to node2.
    * Valid for INTEGER and STRING types only.
    */
    public static CSNode isLessEqualThan(CSNode node1, CSNode node2) {
        // INTEGER comparison
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            if (Integer.parseInt(node1.getName()) <= Integer.parseInt(node2.getName())) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        }

        // STRING comparison (lexicographic)
        else if (node1.getType().equals("STRING") && node2.getType().equals("STRING")) {
            if (node1.getName().compareTo(node2.getName()) <= 0) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } else {
            throw new CSE_Exception("Types do not match");
        }
    }

    /*
    * Static method to check if node1 is greater than or equal to node2.
    * Valid for INTEGER and STRING types only.
    */
    public static CSNode isGreaterEqualThan(CSNode node1, CSNode node2) {
        // INTEGER comparison
        if (node1.getType().equals("INTEGER") && node2.getType().equals("INTEGER")) {
            if (Integer.parseInt(node1.getName()) >= Integer.parseInt(node2.getName())) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        }

        // STRING comparison (lexicographic)
        else if (node1.getType().equals("STRING") && node2.getType().equals("STRING")) {
            if (node1.getName().compareTo(node2.getName()) >= 0) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } else {
            throw new CSE_Exception("Types do not match");
        }
    }

    /*
    * Static method to perform logical OR between two TRUTHVALUE nodes.
    * Returns true if either node is true, false otherwise.
    */
    public static CSNode logicOR(CSNode node1, CSNode node2) {
        if (node1.getType().equals("TRUTHVALUE") && node2.getType().equals("TRUTHVALUE")) {
            if (node1.getName().equals("true") || node2.getName().equals("true")) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } else {
            throw new CSE_Exception("Types do not match");
        }
    }

    /*
    * Static method to perform logical AND between two TRUTHVALUE nodes.
    * Returns true only if both nodes are true, false otherwise.
    */
    public static CSNode logicAND(CSNode node1, CSNode node2) {
        if (node1.getType().equals("TRUTHVALUE") && node2.getType().equals("TRUTHVALUE")) {
            if (node1.getName().equals("true") && node2.getName().equals("true")) {
                return new CSNode("TRUTHVALUE", "true");
            } else {
                return new CSNode("TRUTHVALUE", "false");
            }
        } else {
            throw new CSE_Exception("Types do not match");
        }
    }

    /*
    * Static method to augment a tuple or NIL node with another node.
    * The first node must be of type 'tuple', 'tau', or 'NIL'.
    */
    public static CSNode augment(CSNode node1, CSNode node2) {
        List<String> acceptableTypes = new ArrayList<String>();
        Collections.addAll(acceptableTypes, "tau", "NIL", "tuple");

        if (acceptableTypes.contains(node1.getType())) {
            CSNode augNode = node1.duplicate();
            augNode.setName("tuple");
            augNode.setType("tuple");
            augNode.getTuple().add(node2);
            augNode.setIsTuple(true);
            return augNode;
        } else {
            throw new CSE_Exception("Cannot augment to a non-tuple");
        }
    }
    }

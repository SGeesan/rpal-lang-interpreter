package com.rpal.cse;





/*
 * Class  for Unary Operators
 */
public class UnaryOperations {
    
    /*
     * Function for NOT operator 
     */
    public static CSNode logicNot(CSNode node){
        if (node.getType().equals("TRUTHVALUE")) {
            if (node.getName().equals("true")) {
                return new CSNode("TRUTHVALUE", "false");
            } else {
                return new CSNode("TRUTHVALUE","true");
            }
        } else {
            throw new CSE_Exception("Not a TruthValue type");
        }
    }
    
    /*
     * Function for negative operator
     */
    public static CSNode neg(CSNode node){
        if (node.getType().equals("INTEGER")) {
            int num = Integer.parseInt(node.getName());
            node.setName(String.valueOf(-num));
            return node;
        } else {
            throw new CSE_Exception("Not an INTEGER type");
        }
    }

}


package com.rpal.cse;




/*
 * This class defines a single Environment Node in the Environment Tree.
 * It holds variable bindings for a particular environment context.
 */
public class Node {
    
    private int env_no;             // Unique identifier for the environment
    private CSNode variable;        // Variables and their associated values in this environment
    private Node parentEnv;      // Reference to the parent environment node (if any)

    public Node(int env_no, CSNode variable, Node parentEnv) {
        this.env_no = env_no;
        this.variable = variable;
        this.parentEnv = parentEnv;
    }


    public int getEnv_no() {
        return env_no;
    }

    public void setEnv_no(int env_no) {
        this.env_no = env_no;
    }

    public CSNode getVariable() {
        return variable;
    }

    public void setEnv_variable(CSNode env_variable) {
        this.variable = env_variable;
    }

    public Node getParentEnv() {
        return parentEnv;
    }

    public void setParentEnv(Node parentEnv) {
        this.parentEnv = parentEnv;
    }
    
}


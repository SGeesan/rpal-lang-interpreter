package com.rpal.cse;

/*
 * Class representing a node in the Control Structures and CSE Machine
 */


import java.util.ArrayList;
import java.util.List;


public class CSNode {
    private boolean isTuple;            // Flag to indicate if the node is a tuple
    private List<CSNode> tuple;         // Elements of the tuple, if this node represents a tuple
    private String type;                // Type of the node (e.g., INTEGER, STRING, TRUTHVALUE, lambdaClosure)
    private String name;                // Value of the node (used for INTEGER, STRING, TRUTHVALUE types)
    private List<String> lambdavar;     // Variables enclosed in a lambda closure
    private int lambdano;               // Index of the corresponding delta in the control structure (used in lambda nodes)
    private int envno;                  // Environment number (used in lambda and environment nodes)
    private int thenno;                 // Delta index for the true branch of a beta node
    private int elseno;                 // Delta index for the false branch of a beta node
    private int tauno;                  // Number of elements in a tau node (tuple)

    public CSNode() {
        isTuple = false;
        tuple = new ArrayList<CSNode>();
        lambdavar = new ArrayList<String>();
        type = name = "";
        lambdano = envno = thenno = elseno = tauno = -1;
    }

    // Constructor for simple nodes (e.g., identifiers, constants)
    // Parameters: type and name
    public CSNode(String t, String n) {
        type = t;
        name = n;
        isTuple = false;
        tuple = new ArrayList<CSNode>();
        lambdavar = new ArrayList<String>();
        lambdano = envno = thenno = elseno = tauno = -1;
    }

    // Constructor for lambda closure nodes
    // Parameters: type (should be "lambdaClosure"), list of lambda variables, and delta index
    public CSNode(String t, List<String> varLambda, int lambda_no) {
        type = t;
        lambdavar = varLambda;
        lambdano = lambda_no;
        isTuple = false;
        tuple = new ArrayList<CSNode>();
        name = "";
        envno = thenno = elseno = tauno = -1;
    }

    // Constructor for environment nodes
    // Parameters: type and environment number
    public CSNode(String t, int env_no) {
        type = t;
        envno = env_no;
        isTuple = false;
        tuple = new ArrayList<CSNode>();
        name = "";
        lambdavar = new ArrayList<String>();
        lambdano = thenno = elseno = tauno = -1;
    }

    // Constructor for beta nodes (conditional control structure)
    // Parameters: type (should be "beta"), delta index for true and false branches
    public CSNode(String t, int then_no, int else_no) {
        type = t;
        thenno = then_no;
        elseno = else_no;
        isTuple = false;
        tuple = new ArrayList<CSNode>();
        name = "";
        lambdavar = new ArrayList<String>();
        lambdano = envno = tauno = -1;
    }



    // used to create an object for inserting a delta structure into the control stack 
        // delta no is stored in the envno parameter
    public CSNode(String t, int delta_no, List<CSNode> delta_struct) {
        type = t;
        envno = delta_no;
        tuple = delta_struct;
        isTuple = false;
        name =  "";
        lambdavar = new ArrayList<String>();
        lambdano = thenno = elseno = tauno = -1;
    }

    public boolean getIsTuple() {
        return isTuple;
    }

    public List<CSNode> getTuple() {
        return tuple;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getLambdavar() {
        return lambdavar;
    }

    public int getLambdano() {
        return lambdano;
    }

    public int getEnvno() {
        return envno;
    }

    public int getThenno() {
        return thenno;
    }

    public int getElseno() {
        return elseno;
    }

    public int getTauno() {
        return tauno;
    }

    public void setIsTuple(boolean isTuple) {
        this.isTuple = isTuple;
    }

    public void setTuple(List<CSNode> tuple) {
        this.tuple = tuple;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLambdavar(List<String> lambdavar) {
        this.lambdavar = lambdavar;
    }

    public void setLambdano(int lambdano) {
        this.lambdano = lambdano;
    }

    public void setEnvno(int envno) {
        this.envno = envno;
    }

    public void setThenno(int thenno) {
        this.thenno = thenno;
    }

    public void setElseno(int elseno) {
        this.elseno = elseno;
    }

    public void setTauno(int tauno) {
        this.tauno = tauno;
    }
    
    /*
     * Function to duplicate contents of Control Structure Nodes
     */
    public CSNode duplicate() {
        CSNode dupNode = new CSNode();
        dupNode.setIsTuple(this.getIsTuple());
        dupNode.setTuple(this.getTuple());
        dupNode.setType(this.getType());
        dupNode.setName(this.getName());
        dupNode.setLambdavar(this.getLambdavar());
        dupNode.setLambdano(this.getLambdano());
        dupNode.setEnvno(this.getEnvno());
        dupNode.setThenno(this.getThenno());
        dupNode.setElseno(this.getElseno());
        dupNode.setTauno(this.getTauno());

        return dupNode;
    }

}


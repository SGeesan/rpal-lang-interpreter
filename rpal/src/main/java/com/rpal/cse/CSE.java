package com.rpal.cse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;



/*
 * This class represents the CSE (Control, Stack, Environment) Machine responsible for evaluation operations
 */
public class CSE {
    private List<List<CSNode>> deltaLists;                          // Collection of control structures (deltas)
    private Stack<CSNode> ControlList = new Stack<CSNode>();        // Control stack to manage execution flow
    private Stack<CSNode> StackList = new Stack<CSNode>();          // Operand stack for intermediate values
    private EnvironmentTree envtree = new EnvironmentTree();        // Tree managing all environments created during execution
    private int curr_env = 0;                                       // Identifier for the current active environment
    private int env_counter = 0;                                    // Tracker for the latest environment ID created
    private List<Integer> activeEnvNum = new ArrayList<Integer>();  // Keeps track of currently open environment IDs

    // Constructor to initialize the CSE machine with control structures
    public CSE(List<List<CSNode>> deltaLists) {
        this.deltaLists = deltaLists;
    }

    public int getEnvCounter() {
        return env_counter;
    } 

    /*
     * Adds a specific control structure (delta) to the control stack by its index
     */
    public void insertToControl(int delta_num) {
        List<CSNode> delta_i = deltaLists.get(delta_num);
        CSNode delta_cs = new CSNode("delta", delta_num, delta_i);
        this.getControlList().push(delta_cs);
    }

    /*
     * Breaks down a delta node into its sequence of operations and pushes them onto the control stack
     */
    public void expandDelta() {
        CSNode delta_cs = this.getControlList().pop();

        // Confirm it's a delta node before expanding
        if (delta_cs.getType().equals("delta")) {
            // Push each component of the control structure onto the control stack
            List<CSNode> ctrl_struct = delta_cs.getTuple();
            for (int i = 0; i < ctrl_struct.size(); i++) {
                this.getControlList().push(ctrl_struct.get(i)); 
            }
        } else {
            // Error if the node is not of type delta
            throw new CSE_Exception("Not a Control Structure - cannot be expanded");
        }
    }

    /*
     * Prepares and initializes the control, stack, and environment components for the machine
     */
    private void setupCSE() {
        CSNode parent_env = new CSNode("env", curr_env);              // Create the initial environment node (env 0)

        this.ControlList.push(parent_env);                            // Add the initial env to the control stack
        this.StackList.push(parent_env);                              // Add the initial env to the stack
        this.envtree.addEnv(curr_env, null, null);                    // Insert env 0 into the environment tree (no parent)
        this.activeEnvNum.add(curr_env);                              // Mark env 0 as an active environment

        this.insertToControl(0);                                      // Load the first control structure
        this.expandDelta();                                           // Expand the loaded control structure
    }

    /*
     * Recursively search the environment tree for a variable, starting from a given environment number
     */
    public CSNode lookUpEnv(EnvironmentTree envtree, int env_no, String variable) {
        CSNode envVar = envtree.getEnvNode(env_no).getVariable();
        
        if (envVar==null){
            throw new CSE_Exception("Undefined variable : "+variable);
        }

        // Retrieve the list of variable names defined in this environment
        List<String> varList = envVar.getLambdavar();

        // If the target variable is present in the current environment
        if (varList.contains(variable)) {
            int idx = varList.indexOf(variable);
            
            // Return the corresponding value from the tuple
            return envVar.getTuple().get(idx);
        } else {

            // Otherwise, search recursively in the parent environment
            int parent_no = envtree.getEnvNode(env_no).getParentEnv().getEnv_no();
            return lookUpEnv(envtree, parent_no, variable);
        }
    }


    /*
     * Primary execution method for the CSE Machine
     */
    public void runCSE() {
        setupCSE();                                             // Initialize the CSE machine

        // Continue execution as long as the Control stack has elements
        while (!this.getControlList().empty()) {
            
            CSNode topCtrlNode = this.getControlList().pop();   // Retrieve the current top node from Control  
            CSNode topStackNode1;                               // Placeholder for top item from Stack
            CSNode topStackNode2;                               // Placeholder for second item from Stack

            CSNode newGammaNode;
            CSNode newlambdaNode;

            CSNode valueItem = topCtrlNode.duplicate();
            
            /* Identify the type of the Control node to determine the corresponding rule for execution */
            switch (topCtrlNode.getType()) {
                
                // Rule 1 of CSE
                // Push constants or basic data types directly onto the Stack

                // Integer, String, Boolean, Nil, Dummy, and Y* nodes are stacked without transformation
                case "INTEGER":
                    this.getStackList().push(valueItem);
                    break;
                
                case "STRING":
                    this.getStackList().push(valueItem);
                    break;
                
                case "TRUTHVALUE":
                    this.getStackList().push(valueItem);
                    break;

                case "NIL":
                    this.getStackList().push(valueItem);
                    break;

                case "DUMMY":
                    this.getStackList().push(valueItem);
                    break;

                case "Y":
                    this.getStackList().push(valueItem);
                    break;

                case "IDENTIFIER":

                    // If the identifier matches a built-in function name, push it as-is
                    if (Functions.checkInBuilt(topCtrlNode.getName())) {
                        this.getStackList().push(valueItem);
                        
                    } else {
                        // Otherwise, look up the identifier's bound value from the environment
                        String varName = topCtrlNode.getName();
                        CSNode valueNode = lookUpEnv(envtree, curr_env, varName);
                        this.getStackList().push(valueNode);
                    }

                    break;

                // Rule 2 of CSE
                // Push lambda closures onto the Stack after tagging with current environment
                case "lambdaClosure":
                    topCtrlNode.setEnvno(curr_env);                 // Attach current environment number to the lambda
                    CSNode lambdaNode = topCtrlNode.duplicate();
                    this.StackList.push(lambdaNode);                // Stack the lambda closure
                    break;
  
                // Rules 3, 4, 10, 11, 12, and 13 of CSE: Gamma rule (function application logic)
                case "gamma":
                    topStackNode1 = this.getStackList().pop();

                    // Choose gamma application logic based on type of top item from Stack
                    switch (topStackNode1.getType()) {
                        
                        // Rule 3 of CSE
                        // Apply built-in functions to another argument
                        case "IDENTIFIER":
                            topStackNode2 = this.getStackList().pop();

                            /*
                             * Execute a built-in operation on the second top item using the identifier from the top
                             */

                            switch (topStackNode1.getName()) {
                                case "Print":
                                   Functions.Print(topStackNode2);
                                    this.getStackList().push(topStackNode2);
                                    break;

                                case "Conc":
                                    CSNode concOneNode = Functions.ConcOne(topStackNode2);
                                    this.getStackList().push(concOneNode);
                                    break;

                                case "ConcOne":
                                    CSNode node1 = topStackNode1.getTuple().get(0);
                                    CSNode concatNode = Functions.Conc(node1, topStackNode2);
                                    this.getStackList().push(concatNode);
                                    break;
                                
                                case "Stem":
                                    CSNode stemNode = Functions.Stem(topStackNode2);
                                    this.getStackList().push(stemNode);
                                    break;

                                case "Stern":
                                    CSNode sternNode = Functions.Stern(topStackNode2);
                                    this.getStackList().push(sternNode);
                                    break;

                                case "Order":
                                    CSNode numNode = Functions.Order(topStackNode2);
                                    this.getStackList().push(numNode);
                                    break;

                                case "Null":
                                    CSNode nullNode =Functions.Null(topStackNode2);
                                    this.getStackList().push(nullNode);
                                    break;

                                case "Isinteger":
                                    CSNode isIntNode = Functions.Isinteger(topStackNode2);
                                    this.getStackList().push(isIntNode);
                                    break;
                                
                                case "Istruthvalue":
                                    CSNode isTruthNode = Functions.Istruthvalue(topStackNode2);
                                    this.getStackList().push(isTruthNode);
                                    break;
                                
                                case "Isstring":
                                    CSNode isStringNode = Functions.Isstring(topStackNode2);
                                    this.getStackList().push(isStringNode);
                                    break;
                                
                                case "Istuple":
                                    CSNode isTupleNode = Functions.Istuple(topStackNode2);
                                    this.getStackList().push(isTupleNode);
                                    break;
                                
                                case "Isfunction":
                                    CSNode isFunctionNode = Functions.Isfunction(topStackNode2);
                                    this.getStackList().push(isFunctionNode);
                                    break;
                                
                                case "Isdummy":
                                    CSNode isDummyNode = Functions.Isdummy(topStackNode2);
                                    this.getStackList().push(isDummyNode);
                                    break;

                                case "ItoS":
                                    CSNode strNode = Functions.intToStr(topStackNode2);
                                    this.getStackList().push(strNode);
                                    break;
                                
                                default:
                                    break;
                            }
                            break;

                        // CSE Rule 4 & 11
                        // Apply Lambda (to single and multivariable)
                        case "lambdaClosure":
                            // Obtaining Random Value
                            topStackNode2 = this.getStackList().pop();
                            
                            // moving to next environment
                            env_counter++;
                            this.curr_env = env_counter;

                            // creating new environment variable to insert to control-stack
                            CSNode envCSNode = new CSNode("env", env_counter);

                            // clear space in tuple parameter to insert the values of the parameters tracked by lambda
                            topStackNode1.setTuple(new ArrayList<CSNode>());
                            
                            // if the lambda node tracks multiple parameters (formerly a comma node)
                            if (topStackNode1.getLambdavar().size() > 1) {

                                // then insert each value into the tuple parameter of lambda node
                                List<CSNode> tuple1 = topStackNode2.getTuple();
                                for (int i = 0; i < tuple1.size(); i++) {
                                    topStackNode1.getTuple().add(tuple1.get(i));
                                }

                            } else {
                                // else just save the value in the tuple parameter of the lambda node
                                topStackNode1.getTuple().add(topStackNode2);
                            }
                            CSNode valueNode = topStackNode1.duplicate();

                            // create a new Environment node with value saved 
                            this.envtree.addEnv(curr_env, valueNode, this.envtree.getEnvNode(topStackNode1.getEnvno()));
                            
                            // update environment numbers
                            this.activeEnvNum.add(curr_env);
                            this.setCurr_env(curr_env);

                            // push the new environment node
                            this.getControlList().push(envCSNode);
                            this.getStackList().push(envCSNode);

                            // insert the next delta structure
                            int delta_no = topStackNode1.getLambdano();
                            this.insertToControl(delta_no);
                            this.expandDelta();

                            break;

                        // CSE Rule 10
                        // Tuple Selection
                        case "tuple":
                            // get the index of the element to select from tuple
                            topStackNode2 = this.getStackList().pop();
                            int index_i = Integer.parseInt(topStackNode2.getName());

                            // extract tuple
                            List<CSNode> tuple = topStackNode1.getTuple();
                            
                            // selecting the required tuple element
                            CSNode tup_elem = tuple.get(index_i-1);

                            // inserting the selected tuple element
                            this.getStackList().push(tup_elem);
                            
                            break;
                        
                        // CSE Rule 12
                        // Applying Y to lambda
                        case "Y":
                            topStackNode2 = this.getStackList().pop();
                            CSNode etaNode = topStackNode2.duplicate();
                            etaNode.setType("eta");
                            this.getStackList().push(etaNode);
                            break;
                        
                        // CSE Rule 13
                        // Applying f.p.
                        case "eta":
                            // updating the control
                            newGammaNode = new CSNode("gamma", "gamma");
                            // pushing 2 gamma nodes to the control
                            this.getControlList().push(newGammaNode);      
                            this.getControlList().push(newGammaNode);      

                            // updating the stack
                            List<String> varList = topStackNode1.getLambdavar();
                            
                            // creating a new lambda node with env stored
                            newlambdaNode = new CSNode("lambdaClosure", varList, topStackNode1.getLambdano());
                            newlambdaNode.setEnvno(topStackNode1.getEnvno());
                            this.getStackList().push(topStackNode1);        // pushing the eta node back into the stack
                            this.getStackList().push(newlambdaNode);        // pushing the lambda into the stack
                            break;
                    
                        default:
                            break;
                    }
                    break;
                    /* End of Gamma based rules */

                // CSE Rules 5
                // Exit Environment
                case "env":
                    // value node to be reinserted to stack
                    topStackNode1 = this.getStackList().pop();
                    
                    // environment variable found in stack
                    topStackNode2 = this.getStackList().pop();

                    // checking if the environment variables are matching
                    if (topCtrlNode.getType().equals(topStackNode2.getType()) & 
                                        topCtrlNode.getEnvno() == topStackNode2.getEnvno()){
                        this.getStackList().push(topStackNode1);
                        
                        // unless root environment
                        if (this.curr_env != 0) {
                            // remove the env number from the list of active environments
                            this.activeEnvNum.remove((Integer) curr_env);
                            // set the last unclosed environment in active list as current environment
                            this.curr_env = Collections.max(activeEnvNum);   
                        }
                    } else {
                        // if environments did not match put exception
                        throw new CSE_Exception("Error in Environments");
                    }
                    break;

                    
                // CSE Rules 6
                // Binary Operators
                case "OPERATOR":
                    // obtain the two operands for the binary operation
                    topStackNode1 = this.getStackList().pop();
                    topStackNode2 = this.getStackList().pop();
                    switch (topCtrlNode.getName()) {
                        case "+":
                            CSNode sumNode = BinaryOperations.add(topStackNode1, topStackNode2);
                            this.getStackList().push(sumNode);    
                            break;
                        case "-":
                            CSNode diffNode = BinaryOperations.subtract(topStackNode1, topStackNode2);
                            this.getStackList().push(diffNode);    
                            break;
                        case "*":
                            CSNode productNode =BinaryOperations.multiply(topStackNode1, topStackNode2);
                            this.getStackList().push(productNode);
                            break;
                        case "/":
                            CSNode quotientNode =BinaryOperations.divide(topStackNode1, topStackNode2);
                            this.getStackList().push(quotientNode);
                            break;
                        case "**":
                            CSNode powerNode = BinaryOperations.power(topStackNode1, topStackNode2);
                            this.getStackList().push(powerNode);
                            break;
                        case "eq":
                            CSNode isEqual = BinaryOperations.isEqual(topStackNode1, topStackNode2);
                            this.getStackList().push(isEqual);
                            break;
                        case "ne":
                            CSNode isNotEqual =BinaryOperations.isNotEqual(topStackNode1, topStackNode2);
                            this.getStackList().push(isNotEqual);
                            break;
                        case "ls":
                        case "<":
                            CSNode isLess =BinaryOperations.isLessThan(topStackNode1, topStackNode2);
                            this.getStackList().push(isLess);
                            break;
                        case "gr":
                        case ">":
                            CSNode isGreater = BinaryOperations.isGreaterThan(topStackNode1, topStackNode2);
                            this.getStackList().push(isGreater);
                            break;
                        case "le":
                        case "<=":
                            CSNode isLessEqual = BinaryOperations.isLessEqualThan(topStackNode1, topStackNode2);
                            this.getStackList().push(isLessEqual);
                            break;
                        case "ge":
                        case ">=":
                            CSNode isGreaterEqual = BinaryOperations.isGreaterEqualThan(topStackNode1, topStackNode2);
                            this.getStackList().push(isGreaterEqual);
                            break;
                        case "or":
                            CSNode logicOR = BinaryOperations.logicOR(topStackNode1, topStackNode2);
                            this.getStackList().push(logicOR);
                            break;
                        case "&":
                            CSNode logicAND = BinaryOperations.logicAND(topStackNode1, topStackNode2);
                            this.getStackList().push(logicAND);
                            break;
                        case "aug":
                            CSNode augNode =BinaryOperations.augment(topStackNode1, topStackNode2);
                            this.getStackList().push(augNode);
                            break;
                        default:
                            break;
                    }
                    break;

                // CSE Rules 7
                // Unary Operators
                case "not":
                    topStackNode1 = this.getStackList().pop();
                    this.getStackList().push(UnaryOperations.logicNot(topStackNode1));
                    break;
                case "neg":
                    topStackNode1 = this.getStackList().pop();
                    this.getStackList().push(UnaryOperations.neg(topStackNode1));
                    break;

                // CSE Rules 8
                // Conditional

                case "beta":
                    topStackNode1 = this.getStackList().pop();              // topmost stack element indicating true/false
                    if (topStackNode1.getName().equals("true")) {
                        // insert delta-then
                        this.insertToControl(topCtrlNode.getThenno());      
                        this.expandDelta();
                    } else if (topStackNode1.getName().equals("false")) {
                        // insert delta-else
                        this.insertToControl(topCtrlNode.getElseno());
                        this.expandDelta();
                    }
                    break;
                
                // CSE Rules 9
                // Tuple Formation
                case "tau":
                    // get number of elements in the tuple
                    int n = topCtrlNode.getTauno();
                    
                    // creating the tuple Object to be added into the stack
                    CSNode tuple = new CSNode("tuple", "tuple");
                    tuple.setIsTuple(true);

                    // extracting each of the tuple items from the loop 
                        // and adding to the tuple object
                    for (int i=0; i<n; i++) {
                        CSNode tup_elem = this.getStackList().pop();
                        tuple.getTuple().add(tup_elem.duplicate());
                    }

                    // adding the tuple object to the stack
                    this.getStackList().push(tuple);

                    break;
            
                default:
                    break;
            }

        }

    }
    /*
     * End of runCSE method
     */

    public Stack<CSNode> getControlList() {
        return ControlList;
    }

    public void setControlList(Stack<CSNode> controlList) {
        ControlList = controlList;
    }

    public Stack<CSNode> getStackList() {
        return StackList;
    }

    public void setStackList(Stack<CSNode> stackList) {
        StackList = stackList;
    }

    public int getCurr_env() {
        return curr_env;
    }

    public void setCurr_env(int curr_env) {
        this.curr_env = curr_env;
    }

}



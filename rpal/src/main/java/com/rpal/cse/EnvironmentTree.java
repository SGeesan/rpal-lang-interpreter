package com.rpal.cse;
import java.util.ArrayList;
import java.util.List;




/*
 * Class representing the Environment Tree for the CSE Machine.
 * This tree is implemented as a list of EnvNode instances.
 */
public class EnvironmentTree {
    private List<Node> envList;

    public EnvironmentTree() {
        envList = new ArrayList<Node>();
    }

    /*
     * Adds a new environment node to the tree.
     * Parameters: environment number, associated variable, and its parent environment node.
     */
    public void addEnv(int env_no, CSNode variable, Node parentEnv) {
        Node Node = new Node(env_no, variable, parentEnv);
        envList.add(Node);
    }

    /*
     * Deletes an environment node from the tree based on its environment number.
     */
    public void removeEnv(int env_no){
        for (int i = 0; i < envList.size(); i++) {
            if (envList.get(i).getEnv_no() == env_no) {
                envList.remove(i);
                break;
            }
        }
    }

    /*
     * Retrieves an environment node from the tree by its number.
     * Throws an exception if the node is not found.
     */
    public Node getEnvNode(int env_no) {
        for (int i = 0; i < envList.size(); i++) {
            if (envList.get(i).getEnv_no() == env_no) {
                return envList.get(i);
            }
        }
        throw new CSE_Exception("Missing Environment");
    }
    
}


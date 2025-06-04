package com.rpal.parser;

/*Represents a node in the AST. Uses first child next sibling representation
* 
*                  Node
*                  /  \
*              left    right
*             (child)  (sibling)
*/
public class ASTNode {
    private String type;
    private ASTNode left_child;
    private ASTNode right_child;


    public ASTNode(String type){
        this.type = type ;
        this.left_child = null;
        this.right_child = null;
    }
    
    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }
  
    public void setLeft (ASTNode leftNode){
        this.left_child=leftNode;
    }
    public void setRight (ASTNode rightNode){
        this.right_child=rightNode;
    }
    public ASTNode getLeft (){
        return this.left_child;
    }
    public ASTNode getRight(){
        return this.right_child;
    }
    public void appendRight(ASTNode newNode){
        /*add new sibling to the end of sibling line */
        ASTNode tempNode = this;
        while (tempNode.getRight()!=null){
            tempNode=tempNode.getRight();
        }
        tempNode.setRight(newNode);
    }

    public void printTree() {
        printTreeHelper(this, 0);
    }
    
    private void printTreeHelper(ASTNode node, int depth) {
        if (node == null) {
            return;
        }
    
        // Indent according to depth
        for (int i = 0; i < depth; i++) {
            System.out.print(".");
        }
    
        // Print node
        node.printContent();
    
        // Recur on the child (left node = first child)
        printTreeHelper(node.getLeft(), depth + 1);
    
        // Recur on the sibling (right node = next sibling)
        printTreeHelper(node.getRight(), depth);
    }


    private void printContent() {
        String type = this.getType();
        String value = (this instanceof LeafNode) ? ((LeafNode) this).getValue() : "";
    
        if (type.equals("IDENTIFIER")) {
            System.out.println("<ID:" + value + ">");
        } else if (type.equals("INTEGER")) {
            System.out.println("<INT:" + value + ">");
        } else if (type.equals("STRING")) {
            System.out.println("<STR:" + value + ">");
        } else {
            System.out.println(type+ " ");
        }
    }

}
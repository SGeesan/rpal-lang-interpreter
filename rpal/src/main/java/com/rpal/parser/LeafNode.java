package com.rpal.parser;

/**
 * LeafNode(AST terminal)
 */
public class LeafNode extends ASTNode{
    private String value;

    public LeafNode(String type,String value){
        super(type);
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
    public void setValue(String val){
        this.value = val;
    }
    
    
}

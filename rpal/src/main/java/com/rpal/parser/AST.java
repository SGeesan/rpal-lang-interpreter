package com.rpal.parser;

import java.util.List;

import com.rpal.cse.CSNode;

public class AST {
	private ASTNode root;
	private boolean standardized ;
	public AST(ASTNode root){
		this.root = root;
		this.standardized = false;
	}
    public void standardize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'standardize'");
    }

	public void print() {
		root.printTree();
	}

	public List<List<CSNode>> getCS() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getCS'");
	}

}

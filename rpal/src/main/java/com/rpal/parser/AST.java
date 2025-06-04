package com.rpal.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.rpal.cse.CSNode;

public class AST {
	// Tree details
	private ASTNode root; // main root of the AST
	private boolean standardized; // wheather the tree already standardized

	// Control structure details
	int deltaListLength = 0;// the length of the full delta list
	int currentDeltaID = 0;// ID of the current delta
	List<List<CSNode>> deltaList; // delta list -- list of control structures
	Queue<ASTNode> pendingdelta; // Queue of AST roots for structures 

	public AST(ASTNode root) {
		this.root = root;
		this.standardized = false;
	}

	public void print() {
		/*
		 * Prints the AST
		 */
		root.printTree();
	}

	public boolean isStandardized(ASTNode rooNode) {
		return this.standardized;
	}

	public void standardize() {
		standardizeNode(this.root);
		this.standardized = true;
	}

	private void standardizeNode(ASTNode node) {
		/*
		 * Standardizes the tree from a given node
		 */

		// standardize all child trees left to right
		if (node.getLeft() != null) {
			ASTNode childNode = node.getLeft();
			while (childNode != null) {
				standardizeNode(childNode);
				childNode = childNode.getRight();
			}
		}

		// standardize self using rules
		switch (node.getType()) {
			case "let":
				// standardize let
				ASTNode equalNode = node.getLeft();
				ASTNode exp = equalNode.getLeft().getRight();
				equalNode.getLeft().setRight(equalNode.getRight());
				equalNode.setRight(exp);
				equalNode.setType("lambda");
				node.setType("gamma");
				break;

			case "where":
				// standardize where
				ASTNode lamNode = new ASTNode("lambda");
				lamNode.setRight(node.getLeft().getRight().getLeft().getRight());
				lamNode.setLeft(node.getLeft().getRight().getLeft());
				node.getLeft().setRight(null);
				lamNode.getLeft().setRight(node.getLeft());
				node.setLeft(lamNode);
				node.setType("gamma");
				break;

			case "within":
				// standardize within
				ASTNode x1 = node.getLeft().getLeft();
				ASTNode e1 = x1.getRight();
				ASTNode x2 = node.getLeft().getRight().getLeft();
				ASTNode e2 = x2.getRight();
				lamNode = new ASTNode("lambda");
				x1.setRight(e2);
				lamNode.setLeft(x1);
				lamNode.setRight(e1);
				ASTNode gamNode = new ASTNode("gamma");
				gamNode.setLeft(lamNode);
				x2.setRight(gamNode);
				node.setLeft(x2);
				node.setType("=");
				break;

			case "rec":
				// standardize rec
				equalNode = node.getLeft();
				ASTNode xNode = equalNode.getLeft();
				lamNode = new ASTNode("lambda");
				lamNode.setLeft(xNode);
				ASTNode yNode = new ASTNode("Y");
				yNode.setRight(lamNode);
				gamNode = new ASTNode("gamma");
				gamNode.setLeft(yNode);
				// top x is a copy of x node without linking to e
				LeafNode topX = new LeafNode(xNode.getType(), ((LeafNode) xNode).getValue());
				topX.setLeft(xNode.getLeft());
				topX.setRight(gamNode);
				node.setLeft(topX);
				node.setType("=");
				break;

			case "fcn_form":
				// standardize fcn_form
				ASTNode vbNode = node.getLeft().getRight();
				ASTNode lNode = creteLambdas(vbNode);
				node.getLeft().setRight(lNode);
				node.setType("=");
				break;

			case "@":
				// standardize @
				e1 = node.getLeft();
				ASTNode n = e1.getRight();
				e2 = n.getRight();
				gamNode = new ASTNode("gamma");
				gamNode.setLeft(n);
				n.setRight(e1);
				e1.setRight(null);
				gamNode.setRight(e2);
				node.setLeft(gamNode);
				node.setType("gamma");
				break;

			case "and":
				// standardize and
				equalNode = node.getLeft();
				ASTNode tauNode = new ASTNode("tau");
				ASTNode commNode = new ASTNode("comma");
				tauNode.setLeft(equalNode.getLeft().getRight());
				commNode.setLeft(equalNode.getLeft());
				commNode.getLeft().setRight(null);
				while (equalNode.getRight() != null) {
					equalNode = equalNode.getRight();
					tauNode.getLeft().appendRight(equalNode.getLeft().getRight());
					equalNode.getLeft().setRight(null);
					commNode.getLeft().appendRight(equalNode.getLeft());
				}
				node.setLeft(commNode);
				commNode.setRight(tauNode);
				node.setType("=");
				break;

			case "lambda":
				// standardize lambda
				vbNode = node.getLeft();
				if (vbNode.getRight().getRight() == null) {
					break;
				} else {
					lNode = creteLambdas(vbNode.getRight());
					node.getLeft().setRight(lNode);
					break;
				}

			default:
				break;
		}

	}

	private ASTNode creteLambdas(ASTNode leafNode) {
		/* Recursively converts a chain of variables into nested lambda nodes.*/
		ASTNode lamNode;

		if (leafNode.getRight() != null && leafNode.getRight().getRight() == null) {
			lamNode = new ASTNode("lambda");
			lamNode.setLeft(leafNode);
			return lamNode;
		} else {
			lamNode = new ASTNode("lambda");
			lamNode.setLeft(leafNode);
			leafNode.setRight(creteLambdas(leafNode.getRight()));
			return lamNode;
		}
	}

	public List<List<CSNode>> getCS() {
		/*
		 * Generates and returns the assiciated control structures list
		 */
		deltaListLength = 0;
		currentDeltaID = 0;
		deltaList = new ArrayList<>();
		pendingdelta = new LinkedList<>();

		pendingdelta.add(root);
		while (!pendingdelta.isEmpty()) {
			// create new control structure
			ArrayList<CSNode> currentdelta = new ArrayList<>();
			ASTNode current = pendingdelta.poll();
			// populate the control structure by preorder traversal
			preorder(current, currentdelta);
			// add the new control structure to control structures list
			deltaList.add(currentdelta);
			currentDeltaID++;
		}
		return deltaList;
	}

	public void preorder(ASTNode root, ArrayList<CSNode> currentdelta) {
		/*
		 * traverse the tree from given root adding elements to the given delta
		 */
		//lambda structure
		if (root.getType().equals("lambda")) {
			if (!root.getLeft().getType().equals("comma")) {
				ArrayList<String> name = new ArrayList<String>();
				if (root.getLeft().getType().equals("IDENTIFIER")) {
					String varname = ((LeafNode) root.getLeft()).getValue();
					name.add(varname);
				}
				CSNode lambdaclosure = new CSNode("lambdaClosure", name, ++deltaListLength);
				currentdelta.add(lambdaclosure);
			} else {
				ASTNode commachild = root.getLeft().getLeft();
				ArrayList<String> tuple = new ArrayList<String>();
				while (commachild != null) {
					String name = "";
					if (commachild.getType().equals("IDENTIFIER")) {
						name = ((LeafNode) commachild).getValue();
					}
					tuple.add(name);
					commachild = commachild.getRight();
				}
				CSNode lambdaclosure = new CSNode("lambdaClosure", tuple, ++deltaListLength);
				lambdaclosure.setIsTuple(true);
				currentdelta.add(lambdaclosure);
			}
			pendingdelta.add(root.getLeft().getRight());
			if (root.getRight() != null)
				preorder(root.getRight(), currentdelta);
		}

		// Conditional structure
		else if (root.getType().equals("->")) {
			CSNode betaObject = new CSNode("beta", deltaListLength + 1, deltaListLength + 2);
			currentdelta.add(betaObject);
			pendingdelta.add(root.getLeft().getRight());
			pendingdelta.add(root.getLeft().getRight().getRight());

			root.getLeft().getRight().setRight(null);
			root.getLeft().setRight(null);
			root.setRight(null);
			deltaListLength += 2;
			if (root.getLeft() != null) {
				preorder(root.getLeft(), currentdelta);
			}
			if (root.getRight() != null) {
				preorder(root.getRight(), currentdelta);
			}
		}

		// Tau structure
		else if (root.getType().equals("tau")) {
			String name = "tau";
			String type = "tau";
			int n = 0;
			ASTNode temp = root.getLeft();
			while (temp != null) {
				++n;
				temp = temp.getRight();
			}
			CSNode t = new CSNode(type, name);
			t.setTauno(n);
			currentdelta.add(t);
			if (root.getLeft() != null)
				preorder(root.getLeft(), currentdelta);
			if (root.getRight() != null)
				preorder(root.getRight(), currentdelta);
		}

		else {
			// leaf nodes
			String type = "";
			String name = "";
			if (root.getType().equals("IDENTIFIER")) {
				type = "IDENTIFIER";
				name = ((LeafNode) root).getValue();

			} else if (root.getType().equals("STRING")) {
				type = "STRING";
				name = ((LeafNode) root).getValue();
				name = name.substring(1, name.length() - 1);

			} else if (root.getType().equals("INTEGER")) {
				type = "INTEGER";
				name = ((LeafNode) root).getValue();

			} else if (root.getType().equals("gamma")) {
				type = "gamma";
				name = "gamma";

			} else if (root.getType().equals("Y")) {
				type = "Y";
				name = "Y";

			} else if (root.getType().equals("TRUE")) {
				type = "TRUTHVALUE";
				name = "true";

			} else if (root.getType().equals("FALSE")) {
				type = "TRUTHVALUE";
				name = "false";

			} else if (root.getType().equals("not")) {
				type = "not";
				name = "not";

			} else if (root.getType().equals("neg")) {
				type = "neg";
				name = "neg";

			} else if (root.getType().equals("NIL")) {
				type = "NIL";
				name = "nil";

			} else if (root.getType().equals("DUMMY")) {
				type = "DUMMY";
				name = "dummy";

			} else {
				type = "OPERATOR";
				name = root.getType();
			}

			CSNode t = new CSNode(type, name);

			// Mark NIL as a Tuple Type
			if (t.getType().equals("NIL")) {
				t.setIsTuple(true);
			}

			currentdelta.add(t);

			// traverse children and then sibling
			if (root.getLeft() != null)
				preorder(root.getLeft(), currentdelta);
			if (root.getRight() != null)
				preorder(root.getRight(), currentdelta);
		}
	}

	public int getDeltano() {
		// returns the number of control struvtures for the AST
		return deltaListLength;
	}
}

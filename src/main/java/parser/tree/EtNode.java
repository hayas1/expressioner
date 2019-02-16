package parser.tree;

import visitor.EtVisitor;

public abstract class EtNode {
	private EtNode parent;
	private EtNode[] children;

	public abstract boolean accept(final EtVisitor visitor);

	public EtNode getParent() {
		return this.parent;
	}

	public EtNode setParent(final EtNode parent) {
		this.parent = parent;
		return this;
	}

	public EtNode[] getChildren() {
		return this.children;
	}

//	public EtNode setChildrenNodes(final List<EtNode> children) {
//		this.children = children;
//		return this;
//	}

	public EtNode setChildrenNodes(final EtNode... children) {
		this.children = children;
		return this;
	}

//	public void removeChild(final EtNode node) {
//		if(getChildren().contains(node)) {
//			getChildren().remove(node);
//		} else {
//			throw new NodeTypeException("the node is not this node's child: " + node.toString());
//		}
//	}

//	public void replace(final EtNode node) {
//		System.out.println("this:" + this.hashCode());
//		final EtNode parent = this.getParent();
//		this.setParent(null);
//		node.setParent(parent);
//
//		for(int i=0; i<parent.children.length; i++) {
//			System.out.println(parent.children[i]==this);
//			parent.children[i] = parent.children[i]==this? node: parent.children[i];
//		}
//	}


}

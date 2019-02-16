package parser.tree;

import visitor.EtVisitor;

public abstract class EtNode {
	private EtNode parent;
	private EtNodeChildren children;

	public abstract boolean accept(final EtVisitor visitor);

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}

	public EtNode getParent() {
		return this.parent;
	}

	public EtNode setParent(final EtNode parent) {
		this.parent = parent;
		return this;
	}

	public EtNodeChildren getChildren() {
		return this.children;
	}

//	public EtNode setChildrenNodes(final List<EtNode> children) {
//		this.children = children;
//		return this;
//	}

	public EtNode setChildrenNodes(final EtNode... children) {
		this.children = new EtNodeChildren(children);
		return this;
	}

//	public void removeChild(final EtNode node) {
//		if(getChildren().contains(node)) {
//			getChildren().remove(node);
//		} else {
//			throw new NodeTypeException("the node is not this node's child: " + node.toString());
//		}
//	}

	public void replace(final EtNode node) {
		final EtNode parent = this.getParent();
		this.setParent(null);
		node.setParent(parent);

		parent.getChildren().replace(this, node);
	}


}

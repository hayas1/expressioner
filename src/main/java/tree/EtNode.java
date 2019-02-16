package tree;

import visitor.EtVisitor;

public abstract class EtNode {
	private EtNode parent;
	private EtNodeList children = new EtNodeList();

	public abstract boolean accept(final EtVisitor visitor);

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return getChildren().toString();
	}

	public EtNode getParent() {
		return this.parent;
	}

	public EtNode setParent(final EtNode parent) {
		this.parent = parent;
		return this;
	}

	public EtNodeList getChildren() {
		return this.children;
	}

	public EtNode getChild(final int index) {
		return getChildren().get(index);
	}

	public EtNode setChildrenNodes(final EtNode... children) {
		this.children = new EtNodeList(children);
		return this;
	}

	public EtNode setChild(final int index, final EtNode child) {
		this.children.set(index, child);
		return this;
	}

	public void replace(final EtNode node) {
		final EtNode parent = this.getParent();
		this.setParent(null);
		node.setParent(parent);

		parent.getChildren().replace(this, node);
	}


}

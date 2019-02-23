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


	/**
	 * このノードを引数で指定されたノードで置き換える
	 * つまりこのノードの親への参照を破棄し、指定されたノードの親をこのノードの親であったノードに付け替える
	 * 異なるノードの型で付け替えるとtoString()でキャストのエラーを吐く(保証はしていない)
	 * @param node 付け替えるノード
	 * @return 付け替え後のノード(引数で与えられたノード)
	 */
	public EtNode replace(final EtNode node) {
		final EtNode parent = this.getParent();
		this.setParent(null);
		node.setParent(parent);

		parent.getChildren().replace(this, node);
		return node;
	}

	/**
	 * このノードが引数で指定されたノードと同じ型であるか判定する
	 * @param node 同じ型か判定したいノード
	 * @return 判定結果
	 */
	public boolean isSameNodeType(final EtNode node) {
		return this.getClass()==node.getClass();
	}


}

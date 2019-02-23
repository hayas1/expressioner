package tree;

import visitor.EtVisitor;
import visitor.TermSimplizeVisitor;

/**
 *
 * 主項 -> 項
 * @author hayas
 *
 */
public class MainTerm extends EtNode {
	private static final int TERM = 0;

	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getTerm().accept(visitor);
		}
		return visitor.leave(this);
	}

	@Override
	public MainTerm setParent(final EtNode parent) {
		return (MainTerm) super.setParent(parent);
	}

	public MainTerm setChildren(final Term term) {
		setTerm(term);

		return this;
	}

	@Override
	public String toString() {
		return getTerm().toString();
	}

	public Term getTerm() {
		return (Term)super.getChild(TERM);
	}

	public MainTerm setTerm(final Term term) {
		if(term != null) {
			super.setChild(TERM, term);
		} else {
			throw new NodeTypeException("don't allow null factor");
		}
		return this;
	}



	/**
	 * この主項を分子/分母の形に作り替える
	 * 各因子などの状態は触らない
	 * @return
	 */
	public MainTerm toSimpleFraction() {
		final TermSimplizeVisitor visitor = new TermSimplizeVisitor();
		this.accept(visitor);



		return this;
	}
}

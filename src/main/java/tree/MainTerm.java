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
	 * 親が未設定の項ノードとの掛け算を行う
	 * つまり、この主項の再帰的な子の項のうち、最も子の項の子を持たない項の子に引数の項を設定する
	 * 注：このメソッドは木を組み替えるので探索中に使用すべきではない
	 * @param term 親が未設定の項
	 * @return この項
	 */
	public MainTerm times(final Term term) {
		if(term.getParent()!=null) {
			throw new NodeTypeException("times operand term must have null parent");
		}

		getTerm().times(term);
		return this;
	}

	/**
	 * 親が未設定の項ノードとの割り算を行う
	 * つまり、この主項の再帰的な子の項のうち、最も子の項の子を持たない項の子に除算記号と引数の項を設定する
	 * 注：このメソッドは木を組み替えるので探索中に使用すべきではない
	 * @param factor 親が未設定の累乗因子
	 * @return このノード
	 */
	public MainTerm divide(final Term term) {
		if(term.getParent()!=null) {
			throw new NodeTypeException("divide operand term must have null parent");
		}

		getTerm().divide(term);
		return this;
	}

	public MainTerm times(final MainTerm term) {
		if(term.getParent()!=null) {
			throw new NodeTypeException("times operand term must have null parent");
		}

		final Term child = term.getTerm();
		return this.times(child.setParent(null));
	}

	public MainTerm divide(final MainTerm term) {
		if(term.getParent()!=null) {
			throw new NodeTypeException("divide operand term must have null parent");
		}

		final Term child = term.getTerm();
		return this.divide(child.setParent(null));
	}


	/**
	 * 親が未設定の累乗因子ノードとの掛け算を行う
	 * つまり、累乗因子を子とする新たな項を作成し、その項の子との間に挿入する
	 * @param factor 親が未設定の累乗因子
	 * @return 掛け算後の部分木の項のルート つまり新たに作成した項のノード
	 */
	public MainTerm times(final PowerFactor factor) {
		if(factor.getParent()!=null) {
			throw new NodeTypeException("times operand power factor must have null parent");
		}

		final Term child = getTerm();
		final Term newTerm = Term.makeNode(factor);
		newTerm.setTerm(child.setParent(newTerm));
		this.setTerm(newTerm.setParent(this));

		return this;
	}

	public MainTerm divide(final PowerFactor factor) {
		if(factor.getParent()!=null) {
			throw new NodeTypeException("divide operand power factor must have null parent");
		}

		getTerm().divide(factor);
		return this;
	}


	/**
	 * この主項を分子/(分母)の形に作り替える
	 * 各因子などの状態は触らない
	 * @return
	 */
	public MainTerm toSimpleFraction() {
		final TermSimplizeVisitor visitor = new TermSimplizeVisitor();
		this.accept(visitor);



		return this;
	}
}

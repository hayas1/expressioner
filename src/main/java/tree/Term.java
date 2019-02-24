package tree;

import token.Operator;
import visitor.EtVisitor;

/**
 * @author hayas
 * 項 = 因子 [[乗法演算子] 項]
 *
 */
public class Term extends EtNode {
	private static final int POWER_FACTOR = 0;
	private Operator operator;
	private static final int TERM = 1;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getPowerFactor().accept(visitor);
			if(hasTerm()) {
				getTerm().accept(visitor);
			}
		}
		return visitor.leave(this);
	}

	@Override
	public Term setParent(final EtNode parent) {
		return (Term) super.setParent(parent);
	}

	public Term setChildren(final PowerFactor factor, final Operator operator, final Term term) {
		setPowerFactor(factor);
		setOperator(operator);
		setTerm(term);

		return this;
	}

	@Override
	public String toString() {
		if(hasOperator() && hasTerm()) {
			return getPowerFactor().toString() + getOperator().toString() + getTerm().toString();
		} else if(!hasOperator() && hasTerm()) {
			return getPowerFactor().toString() + Operator.TIMES + getTerm().toString();
		} else {
			return getPowerFactor().toString();
		}
	}

	public PowerFactor getPowerFactor() {
		return (PowerFactor)super.getChild(POWER_FACTOR);
	}

	public Term setPowerFactor(final PowerFactor factor) {
		if(factor != null) {
			super.setChild(POWER_FACTOR, factor);
		} else {
			throw new NodeTypeException("don't allow null factor");
		}
		return this;
	}

	public boolean hasOperator() {
		final boolean hasOperator = getOperator()!=null;
		final boolean hasTerm = getTerm()!=null;
		if(hasOperator && !hasTerm) {
			throw new NodeTypeException("invalid term type: powerFactor operator null");
		} else {
			return hasOperator;
		}
	}

	public Operator getOperator() {
		return operator;
	}

	public Term setOperator(final Operator operator) {
		if(operator==null || operator.isMultiplicativeOperator()) {
			this.operator = operator;
		} else {
			throw new NodeTypeException("not multiplicative operator: " + operator.getName());
		}
		return this;
	}

	public boolean hasTerm() {
		final boolean hasOperator = getOperator()!=null;
		final boolean hasTerm = getTerm()!=null;
		if(hasOperator && !hasTerm) {
			throw new NodeTypeException("invalid term type: powerFactor operator null");
		} else {
			return hasTerm;
		}
	}

	public Term getTerm() {
		return (Term)super.getChild(TERM);
	}

	public Term setTerm(final Term term) {
		super.setChild(TERM, term);
		return this;
	}


	public boolean isTimes() {
		if(hasOperator()) {
			return getOperator().isTimes();
		} else {
			return hasTerm();
		}
	}

	public boolean isDivide() {
		if(hasOperator()) {
			return getOperator().isDivide();
		} else {
			return false;
		}
	}

	/**
	 * 親が未設定の累乗因子ノードから、そのノードのみを子にもち親が未設定の新たな項のノードを作成する
	 * 注：このメソッドは木を組み替えるので木の探索中に使用すべきではない
	 * @param powerFactor 親が未設定の累乗因子
	 * @return 作成したノード
	 */
	public static Term makeNode(final PowerFactor factor) {
		if(factor.getParent()!=null) {
			throw new NodeTypeException("term child must have null parent");
		}

		final Term term = new Term();
		term.setPowerFactor(factor.setParent(term));

		return term;
	}

	public Term getDeepestTerm() {
		Term downer;
		for(downer = this; downer.hasTerm(); downer = downer.getTerm());
		return downer;
	}

	/**
	 * 親が未設定の項ノードとの掛け算を行う
	 * つまり、この項の再帰的な子の項のうち、最も子の項の子を持たない項の子に引数の項を設定する
	 * 注：このメソッドは木を組み替えるので木の探索中に使用すべきではない
	 * @param term 親が未設定の項
	 * @return この項
	 */
	public Term times(final Term term) {
		if(term.getParent()!=null) {
			throw new NodeTypeException("times operand term must have null parent");
		}

		final Term deepest = getDeepestTerm();
		deepest.setTerm(term.setParent(deepest));

		return this;
	}

	/**
	 * 親が未設定の主項ノードとの割り算を行う
	 * つまり、この項の再帰的な子の項のうち、最も子の項の子を持たない項の子に除算記号と引数の項を設定する
	 * 注：このメソッドは木を組み替えるので木の探索中に使用すべきではない
	 * @param factor 親が未設定の累乗因子
	 * @return このノード
	 */
	public Term divide(final Term term) {
		if(term.getParent()!=null) {
			throw new NodeTypeException("divide operand term must have null parent");
		}

		final Term deepest = getDeepestTerm();
		deepest.setOperator(Operator.create(Operator.DIVIDE)).setTerm(term.setParent(deepest));

		return this;
	}

	public Term times(final PowerFactor factor) {
		if(factor.getParent()!=null) {
			throw new NodeTypeException("times operand power factor must have null parent");
		}

		return this.times(makeNode(factor));
	}

	public Term divide(final PowerFactor factor) {
		if(factor.getParent()!=null) {
			throw new NodeTypeException("divide operand power factor must have null parent");
		}

		return this.divide(makeNode(factor));
	}

}

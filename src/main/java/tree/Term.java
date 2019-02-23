package tree;

import token.Operator;
import visitor.EtVisitor;
import visitor.TermSimplizeVisitor;

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
		setFactor(factor);
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

	public Term setFactor(final PowerFactor factor) {
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
	 * 親が未設定の累乗因子ノードとの掛け算を行う
	 * つまり、新たな項を作成し、その項の親にこの項の親を設定し、その項の子として引数の累乗因子とこの項を設定する
	 * @param factor 親が未設定の累乗因子
	 * @return 掛け算後の部分木の項のルート つまり新たに作成した項のノード
	 */
	public Term times(final PowerFactor factor) {
		if(factor.getParent()!=null) {
			throw new NodeTypeException("times operand must have null parent");
		}

		final Term term = new Term();
		this.replace(term);
		term.setChildren(factor, null, this);
		this.setParent(term);
		factor.setParent(term);

		return term;
	}

	/**
	 * 親が未設定の累乗因子ノードとの割り算を行う
	 * つまり、この項の子の項のうち、最も子の項の子を持たない項の子に除算記号と引数の累乗因子を設定する
	 * このとき項と累乗因子の間に新たに項のノードが作成される
	 * @param factor
	 * @return
	 */
	public Term Divide(final PowerFactor factor) {
		if(factor.getParent()!=null) {
			throw new NodeTypeException("divide operand must have null parent");
		}

	}

	/**
	 * この項を分子/分母の形に作り替える
	 * 各因子などの状態は触らない これは式の直下の項について行うことを前提としている
	 * @return
	 */
	public Term toSimpleFraction() {
		final TermSimplizeVisitor visitor = new TermSimplizeVisitor();
		this.accept(visitor);



		return this;
	}
}

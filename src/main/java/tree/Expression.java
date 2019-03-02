package tree;

import token.Operator;
import visitor.EtVisitor;

/**
 *
 * 式 -> 主項 [加法演算子 式]
 * @author hayas
 *
 */
public class Expression extends EtNode {
	private static final int MAIN_TERM = 0;
	private Operator operator;
	private static final int EXPRESSION = 1;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getMainTerm().accept(visitor);
			if(hasOperator()) {
				getExpression().accept(visitor);
			}
		}

		return visitor.leave(this);
	}

	@Override
	public Expression setParent(final EtNode parent) {
		return (Expression)super.setParent(parent);
	}

	public Expression setChildren(final MainTerm term, final Operator operator, final Expression expression) {
		setMainTerm(term);
		setOperator(operator);
		setExpression(expression);

		return this;
	}

	@Override
	public String toString() {
		if(hasOperator()) {
			return getMainTerm().toString() + getOperator().toString() + getExpression().toString();
		} else {
			return getMainTerm().toString();
		}
	}

	@Override
	public Expression copySubEt(final EtNode parent) {
		final Expression expression = new Expression();

		final MainTerm term = getMainTerm().copySubEt(expression);
		final Operator operator = hasOperator()? getOperator(): null;
		final Expression postExpression = hasOperator()? getExpression().copySubEt(expression): null;

		return expression.setParent(parent).setChildren(term, operator, postExpression);
	}

	public MainTerm getMainTerm() {
		return (MainTerm)super.getChild(MAIN_TERM);
	}

	public Expression setMainTerm(final MainTerm term) {
		if(term != null) {
			super.setChild(MAIN_TERM, term);
		} else {
			throw new NodeTypeException("don't allow null term");
		}
		return this;
	}

	public boolean hasOperator() {
		final boolean hasOperator = getOperator()!=null;
		final boolean hasExpression = getExpression()!=null;
		if(hasOperator && !hasExpression) {
			throw new NodeTypeException("invalid expression type: term additiveoperator null");
		} else if(!hasOperator && hasExpression) {
			throw new NodeTypeException("invalid exspression type: term null expression");
		} else {
			return hasOperator;
		}
	}

	public Operator getOperator() {
		return operator;
	}

	public Expression setOperator(final Operator operator) {
		if(operator==null || operator.isAdditiveOperator()) {
			this.operator = operator;
		} else {
			throw new NodeTypeException("not additive operator: " + operator.getName());
		}
		return this;
	}

	public Expression getExpression() {
		return (Expression)super.getChild(EXPRESSION);
	}

	public Expression setExpression(final Expression expression) {
		super.setChild(EXPRESSION, expression);
		return this;
	}


//	public Expression reverseSign() {
//		if(hasOperator()) {
//			setOperator(getOperator().reverseOperator());
//		}
//		return this;
//	}

	/**
	 * 新たに括弧式、主式のノードを作成し、それらの親子関係を作成
	 * その子として、この式の部分木をディープコピーした、新たな括弧式を作成する
	 * @return 作成した括弧式
	 */
	public ParenedExpression makeParened() {
		final ParenedExpression parened = new ParenedExpression();
		final MainExpression mainExpression = new MainExpression();

		parened.setParent(null).setExpression(mainExpression);
		mainExpression.setParent(parened).setExpression(this.getExpression().copySubEt(mainExpression));

		return parened;
	}
}

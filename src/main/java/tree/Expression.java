package tree;

import lexer.tokens.Operator;
import visitor.EtVisitor;

/**
 *
 * 式 -> 項 [加法演算子 式]
 * @author hayas
 *
 */
public class Expression extends EtNode {
	private static final int TERM = 0;
	private Operator operator;
	private static final int EXPRESSION = 1;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getTerm().accept(visitor);
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

	public Expression setChildren(final Term term, final Operator operator, final Expression expression) {
		setTerm(term);
		setOperator(operator);
		setExpression(expression);

		return this;
	}

	@Override
	public String toString() {
		if(hasOperator()) {
			return getTerm().toString() + getOperator().toString() + getExpression().toString();
		} else {
			return getTerm().toString();
		}
	}


	public Term getTerm() {
		return (Term)super.getChild(TERM);
	}

	public Expression setTerm(final Term term) {
		if(term != null) {
			super.setChild(TERM, term);
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



}

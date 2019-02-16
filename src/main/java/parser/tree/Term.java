package parser.tree;

import lexer.tokens.Operator;
import visitor.EtVisitor;

/**
 * @author hayas
 * 項 = 因子 [[乗法演算子] 項]
 *
 */
public class Term extends EtNode {
	private PowerFactor factor;
	private Operator operator;
	private Term term;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getFactor().accept(visitor);
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

		return (Term) super.setChildrenNodes(factor, term);
	}

	@Override
	public String toString() {
		if(hasOperator() && hasTerm()) {
			return getFactor().toString() + getOperator().toString() + getTerm().toString();
		} else if(!hasOperator() && hasTerm()) {
			return getFactor().toString() + getTerm().toString();
		} else {
			return getFactor().toString();
		}
	}

	public PowerFactor getFactor() {
		return factor;
	}

	public Term setFactor(final PowerFactor factor) {
		if(factor != null) {
			this.factor = factor;
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
		return term;
	}

	public Term setTerm(final Term term) {
		this.term = term;
		return this;
	}



}

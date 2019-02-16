package tree;

import lexer.tokens.Operator;
import visitor.EtVisitor;

/**
 *
 * 累乗因子 -> 因子 [累乗演算子 累乗因子]
 * @author hayas
 *
 */
public class PowerFactor extends Argument {
	private static final int FACTOR = 0;
	private Operator operator;
	private static final int POWER_FACTOR = 1;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getFactor().accept(visitor);
			if(hasOperator()) {
				getPowerFactor().accept(visitor);
			}
		}

		return visitor.visit(this);
	}

	@Override
	public PowerFactor setParent(final EtNode parent) {
		return (PowerFactor) super.setParent(parent);
	}

	public PowerFactor setChildren(final Factor factor, final Operator operator, final PowerFactor powerFactor) {
		setFactor(factor);
		setOperator(operator);
		setPowerFactor(powerFactor);

		return this;
	}

	@Override
	public String toString() {
		if(hasOperator()) {
			return getFactor().toString() + getOperator().toString() + getPowerFactor().toString();
		} else {
			return getFactor().toString();
		}
	}

	public Factor getFactor() {
		return (Factor)super.getChild(FACTOR);
	}

	public PowerFactor setFactor(final Factor factor) {
		if(factor != null) {
			super.setChild(FACTOR, factor);
		} else {
			throw new NodeTypeException("don't allow null factor");
		}
		return this;
	}

	public boolean hasOperator() {
		final boolean hasOperator = getOperator()!=null;
		final boolean hasPowerFactor = getPowerFactor()!=null;
		if(hasOperator && !hasPowerFactor) {
			throw new NodeTypeException("invalid power factor type: factor powerOperator null");
		} else if(!hasOperator && hasPowerFactor) {
			throw new NodeTypeException("invalid power factor type: factor null powerFactor");
		} else {
			return hasOperator;
		}
	}

	public Operator getOperator() {
		return operator;
	}

	public PowerFactor setOperator(final Operator operator) {
		if(operator==null || operator.isPower()) {
			this.operator = operator;
		} else {
			throw new NodeTypeException("not power operator: " + operator.getName());
		}
		return this;
	}

	public PowerFactor getPowerFactor() {
		return (PowerFactor)super.getChild(POWER_FACTOR);
	}

	public PowerFactor setPowerFactor(PowerFactor powerFactor) {
		super.setChild(POWER_FACTOR, powerFactor);
		return this;
	}



}

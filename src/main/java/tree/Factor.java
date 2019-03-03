package tree;

import token.Dummy;
import token.Operator;

/**
 * 因子 -> 変数 | 定数 | 因子式 | 関数
 * @author hayas
 *
 */

public abstract class Factor extends EtNode {
	public static final String VARIABLE = "variable";
	public static final String CONSTANT = "constant";
	public static final String FACTOR_EXPRESSION = "factorExpression";
	public static final String FUNCTION = "function";

	private Operator powerOperator;

	//public abstract String getFactorType();

	@Override
	public abstract Factor copySubEt(final EtNode parent);

	@Override
	public Factor setParent(EtNode parent) {
		return (Factor) super.setParent(parent);
	}

	public String getPowerOperatorString() {
		if(hasPowerOperator()) {
			return getPowerOperator().toString();
		} else {
			return Dummy.DUMMY;
		}
	}

	public boolean hasPowerOperator() {
		return getPowerOperator() != null;
	}

	public Operator getPowerOperator() {
		return powerOperator;
	}

	public Factor setPowerOperator(final Operator powerOperator) {
		if(powerOperator==null || powerOperator.isPower()) {
			this.powerOperator = powerOperator;
		} else {
			throw new NodeTypeException("not power operator: " + powerOperator.getName());
		}
		return this;
	}

}
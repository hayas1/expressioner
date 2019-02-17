package tree;

import token.Operator;
import visitor.EtVisitor;

/**
 *
 * 条件 -> 主式 関係演算子 主式
 * @author hayas
 *
 */

public class Condition extends EtNode {
	private final static int LEFT_HAND_SIDE = 0;
	private Operator relationalOperator;
	private final static int RIGHT_HAND_SIDE = 1;

	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getLeftHandSide().accept(visitor);
			getRightHandSide().accept(visitor);
		}
		return visitor.leave(this);
	}

	@Override
	public Condition setParent(final EtNode parent) {
		return (Condition) super.setParent(parent);
	}

	public Condition setChildren(final MainExpression leftHandSide, final Operator operator, final MainExpression rightHandSide) {
		setLeftHandSide(leftHandSide);
		setRelationalOperator(operator);
		setRightHandSide(rightHandSide);

		return this;
	}

	@Override
	public String toString() {
		return getLeftHandSide().toString() + getRelationalOperator().toString() + getRightHandSide().toString();
	}

	public MainExpression getLeftHandSide() {
		return (MainExpression)super.getChild(LEFT_HAND_SIDE);
	}

	public Condition setLeftHandSide(final MainExpression leftHandSide) {
		if(leftHandSide != null) {
			super.setChild(LEFT_HAND_SIDE, leftHandSide);
		} else {
			throw new NodeTypeException("don't allow null left hand side expression");
		}
		return this;
	}

	public Operator getRelationalOperator() {
		return relationalOperator;
	}

	public Condition setRelationalOperator(final Operator operator) {
		if(operator==null || operator.isRelationalOperator()) {
			this.relationalOperator = operator;
		} else {
			throw new NodeTypeException("not relational operator: " + operator.getName());
		}
		return this;
	}

	public MainExpression getRightHandSide() {
		return (MainExpression)super.getChild(RIGHT_HAND_SIDE);
	}

	public Condition setRightHandSide(final MainExpression rightHandSide) {
		if(rightHandSide != null) {
			super.setChild(RIGHT_HAND_SIDE, rightHandSide);
		} else {
			throw new NodeTypeException("don't allow null right hand side expression");
		}
		return this;
	}

}

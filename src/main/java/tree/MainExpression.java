package tree;

import token.Operator;
import visitor.EtVisitor;

/**
 *
 * 主式 -> [符号] 式
 * @author hayas
 *
 */
public class MainExpression extends EtNode{
	private Operator sign;
	private static final int EXPRESSION = 0;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getExpression().accept(visitor);
		}
		return visitor.leave(this);
	}

	@Override
	public MainExpression setParent(final EtNode parent) {
		return (MainExpression)super.setParent(parent);
	}

	public MainExpression setChildren(final Operator sign, final Expression expression) {
		setSign(sign);
		setExpression(expression);

		return this;
	}

	@Override
	public String toString() {
		if(hasSign()) {
			return getSign().toString() + getExpression().toString();
		} else {
			return getExpression().toString();
		}
	}

	public boolean hasSign() {
		return getSign() != null;
	}

	public Operator getSign() {
		return sign;
	}

	public MainExpression setSign(final Operator sign) {
		if(sign==null || sign.isSignOperator()) {
			this.sign = sign;
		} else {
			throw new NodeTypeException("not sign operator token: " + sign.getName());
		}
		return this;
	}

	public Expression getExpression() {
		return (Expression)super.getChild(EXPRESSION);
	}

	public MainExpression setExpression(final Expression expression) {
		if(expression != null) {
			super.setChild(EXPRESSION, expression);
		} else {
			throw new NodeTypeException("don't allow null expression");
		}
		return null;
	}

}

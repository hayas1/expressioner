package parser.tree;

import lexer.tokens.Paren;
import visitor.EtVisitor;

/**
 *
 * 因子式 -> 左括弧 主式 右括弧
 * @author hayas
 *
 */
public class FactorExpression extends Factor {
	private Paren leftParen;
	private final static int MAIN_EXPRESSION = 0;
	private Paren rightParen;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getExpression().accept(visitor);
		}
		return visitor.leave(this);
	}

	@Override
	public FactorExpression setParent(final EtNode parent) {
		return (FactorExpression)super.setParent(parent);
	}

	public FactorExpression setChildren(final Paren leftParen, final MainExpression expression, final Paren rightParen) {
		setLeftParen(leftParen);
		setExpression(expression);
		setRightParen(rightParen);

		return this;
	}

	@Override
	public String toString() {
		return getLeftParen().toString() + getExpression().toString() + getRightParen().toString();
	}


	public Paren getLeftParen() {
		return leftParen;
	}

	public FactorExpression setLeftParen(Paren leftParen) {
		if(leftParen.isLeftParen()) {
			this.leftParen = leftParen;
		} else {
			throw new NodeTypeException("not left paren token: " + leftParen.getName());
		}
		return this;
	}

	public MainExpression getExpression() {
		return (MainExpression)super.getChild(MAIN_EXPRESSION);
	}

	public FactorExpression setExpression(final MainExpression expression) {
		if(expression != null) {
			super.setChild(MAIN_EXPRESSION, expression);
		} else {
			throw new NodeTypeException("don't allow null expression");
		}
		return this;
	}

	public Paren getRightParen() {
		return rightParen;
	}

	public FactorExpression setRightParen(final Paren rightParen) {
		if(rightParen.isRightParen()) {
			this.rightParen = rightParen;
		} else {
			throw new NodeTypeException("not right paren token: " + leftParen.getName());
		}
		return this;
	}




}

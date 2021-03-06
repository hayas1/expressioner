package tree;

import java.util.List;
import java.util.stream.Collectors;

import token.Paren;
import token.Separator;
import visitor.EtVisitor;

/**
 *
 * 式列 -> 左括弧 主式 {"," 主式}  右括弧
 * @author hayas
 *
 */

public class Expressions extends Argument {
	private Paren leftParen;
	private Paren rightParen;

	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getExpressions().forEach(exp -> exp.accept(visitor));
		}
		return visitor.leave(this);
	}

	@Override
	public Expressions setParent(final EtNode parent) {
		return (Expressions) super.setParent(parent);
	}

	public Expressions setChildren(final Paren leftParen, final List<MainExpression> expressions, final Paren rightParen) {
		setLeftParen(leftParen);
		setExpressions(expressions);
		setRightParen(rightParen);

		return this;
	}

	@Override
	public String toString() {
		final String row = getExpressions()
				.stream()
				.map(MainExpression::toString)
				.collect(Collectors.joining(Separator.COMMA + " "));
		return getLeftParen().toString() + row + getRightParen().toString();
	}


	public Paren getLeftParen() {
		return leftParen;
	}

	public Expressions setLeftParen(final Paren leftParen) {
		if(leftParen==null || leftParen.isLeftParen()) {
			this.leftParen = leftParen;
		} else {
			throw new NodeTypeException("not left paren token: " + leftParen.getName());
		}
		return this;
	}

	public List<MainExpression> getExpressions() {
		return super.getChildren().downCast(MainExpression.class);
	}

	public Expressions setExpressions(final List<MainExpression> expressions) {
		if(expressions!=null && !expressions.isEmpty()) {
			super.setChildrenNodes(expressions.toArray(new EtNode[expressions.size()]));
		} else {
			throw new NodeTypeException("don't allow null or empty expressions");
		}
		return this;
	}

	public Paren getRightParen() {
		return rightParen;
	}

	public Expressions setRightParen(final Paren rightParen) {
		if(rightParen==null || rightParen.isRightParen()) {
			this.rightParen = rightParen;
		} else {
			throw new NodeTypeException("not right paren token: " + leftParen.getName());
		}
		return this;
	}

	public int getArgumentNum() {
		return getExpressions().size();
	}

	public MainExpression getArgument() {
		if(getArgumentNum() == 1) {
			return getExpressions().get(0);
		} else {
			throw new NodeTypeException("this method call only if argument num = 1, but num: " + getArgumentNum());
		}
	}

}

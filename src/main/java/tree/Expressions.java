package tree;

import java.util.List;
import java.util.stream.Collectors;

import token.Paren;
import token.Separator;
import visitor.EtVisitor;

/**
 *
 * 式列 -> [左括弧] 式 {"," 式} [右括弧]
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

	public Expressions setChildren(final Paren leftParen, final List<Expression> expressions, final Paren rightParen) {
		setLeftParen(leftParen);
		setExpressions(expressions);
		setRightParen(rightParen);

		return this;
	}

	@Override
	public String toString() {
		final String leftParen = hasLeftParen()? getLeftParen().toString(): "";
		final String row = getExpressions()
				.stream()
				.map(Expression::toString)
				.collect(Collectors.joining(Separator.COMMA + " "));
		final String rightParen = hasRightParen()? getRightParen().toString(): "";

		return leftParen + row + rightParen;
	}

	@Override
	public Expressions copySubEt(final EtNode parent) {
		final Expressions expressions = new Expressions();

		final Paren leftParen = hasLeftParen()? getLeftParen().clone(): null;
		final List<Expression> Expressions = getExpressions()
				.stream()
				.map(exp -> exp.copySubEt(expressions))
				.collect(Collectors.toList());
		final Paren rightParen = hasRightParen()? getRightParen().clone(): null;

		return expressions.setParent(parent).setChildren(leftParen, Expressions, rightParen);
	}

	public boolean hasLeftParen() {
		return getLeftParen() != null;
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

	public List<Expression> getExpressions() {
		return super.getChildren().downCast(Expression.class);
	}

	public Expressions setExpressions(final List<Expression> expressions) {
		if(expressions!=null && !expressions.isEmpty()) {
			super.setChildrenNodes(expressions.toArray(new EtNode[expressions.size()]));
		} else {
			throw new NodeTypeException("don't allow null or empty expressions");
		}
		return this;
	}

	public boolean hasRightParen() {
		return getRightParen() != null;
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

	public Expression getArgument() {
		if(getArgumentNum() == 1) {
			return getExpressions().get(0);
		} else {
			throw new NodeTypeException("this method call only if argument num = 1, but num: " + getArgumentNum());
		}
	}

}

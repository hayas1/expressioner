package tree;

import java.util.List;
import java.util.stream.Collectors;

import token.ConstantToken;
import token.Paren;
import token.Separator;
import visitor.DefinitionVisitor;

/**
 *
 * 定数列 -> [左括弧] 定数 {"," 定数}  [右括弧]
 * @author hayas
 *
 */

public class Constants extends DefinitionNode {

	private Paren leftParen;
	private List<ConstantToken> constants;
	private Paren rightParen;

	@Override
	public boolean accept(final DefinitionVisitor visitor) {
		visitor.visit(this);
		return visitor.leave(this);
	}

	@Override
	public Constants setParent(final EtNode parent) {
		return (Constants) super.setParent(parent);
	}

	public Constants setChildren(final Paren leftParen, final List<ConstantToken> constants, final Paren rightParen) {
		setLeftParen(leftParen);
		setConstants(constants);
		setRightParen(rightParen);

		return this;
	}

	@Override
	public String toString() {
		final String leftParen = hasLeftParen()? getLeftParen().toString(): "";
		final String rightParen = hasRightParen()? getRightParen().toString(): "";
		final String row = constants
				.stream()
				.map(ConstantToken::toString)
				.collect(Collectors.joining(Separator.COMMA + " "));
		return leftParen + row + rightParen;
	}

	@Override
	public Constants copySubEt(final EtNode parent) {
		final Constants constants = new Constants();

		final Paren leftParen = hasLeftParen()? getLeftParen(): null;
		final List<ConstantToken> constantList = getConstants()
				.stream()
				.map(ConstantToken::clone)
				.collect(Collectors.toList());
		final Paren rightParen = hasRightParen()? getRightParen(): null;

		return constants.setParent(parent).setChildren(leftParen, constantList, rightParen);
	}

	public boolean hasLeftParen() {
		return getLeftParen() != null;
	}

	public Paren getLeftParen() {
		return leftParen;
	}

	public Constants setLeftParen(final Paren leftParen) {
		if(leftParen==null || leftParen.isLeftParen()) {
			this.leftParen = leftParen;
		} else {
			throw new NodeTypeException("not left paren token: " + leftParen.getName());
		}
		return this;
	}

	public List<ConstantToken> getConstants() {
		return constants;
	}

	public Constants setConstants(final List<ConstantToken> constants) {
		if(constants != null) {
			this.constants = constants;
		} else {
			throw new NodeTypeException("don't allow null constant");
		}
		return this;
	}

	public boolean hasRightParen() {
		return getRightParen()!=null;
	}

	public Paren getRightParen() {
		return rightParen;
	}

	public Constants setRightParen(final Paren rightParen) {
		if(rightParen==null || rightParen.isRightParen()) {
			this.rightParen = rightParen;
		} else {
			throw new NodeTypeException("not right paren token: " + leftParen.getName());
		}
		return this;
	}

	public int getParameterNum() {
		return getConstants().size();
	}

	public ConstantToken getParameter() {
		if(getParameterNum() == 1) {
			return getConstants().get(0);
		} else {
			throw new NodeTypeException("this method call only if parameter num = 1, but num: " + getParameterNum());
		}
	}

}

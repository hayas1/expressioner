package parser.tree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lexer.tokens.ConstantToken;
import lexer.tokens.Paren;
import lexer.tokens.Separator;
import lexer.tokens.Token;
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
		final Token leftParen = Optional.ofNullable((Token)getLeftParen()).orElse(Token.createDummy());
		final Token rightParen = Optional.ofNullable((Token)getRightParen()).orElse(Token.createDummy());
		final String row = constants
				.stream()
				.map(ConstantToken::toString)
				.collect(Collectors.joining(Separator.COMMA + " "));
		return leftParen.toString() + row + rightParen.toString();
	}


	public Paren getLeftParen() {
		return leftParen;
	}

	public Constants setLeftParen(final Paren leftParen) {
		if(leftParen.isLeftParen()) {
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

	public Paren getRightParen() {
		return rightParen;
	}

	public Constants setRightParen(final Paren rightParen) {
		if(rightParen.isRightParen()) {
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

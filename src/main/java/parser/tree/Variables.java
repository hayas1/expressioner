package parser.tree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lexer.tokens.Paren;
import lexer.tokens.Separator;
import lexer.tokens.Token;
import lexer.tokens.VariableToken;
import visitor.DefinitionVisitor;

/**
 *
 * 変数列 -> [左括弧] 変数 {"," 変数}  [右括弧]
 * @author hayas
 *
 */

public class Variables extends DefinitionNode {

	private Paren leftParen;
	private List<VariableToken> variables;
	private Paren rightParen;

	@Override
	public boolean accept(final DefinitionVisitor visitor) {
		visitor.visit(this);
		return visitor.leave(this);
	}

	@Override
	public Variables setParent(final EtNode parent) {
		return (Variables) super.setParent(parent);
	}

	public Variables setChildren(final Paren leftParen, final List<VariableToken> variables, final Paren rightParen) {
		setLeftParen(leftParen);
		setVariables(variables);
		setRightParen(rightParen);

		return this;
	}

	@Override
	public String toString() {
		final Token leftParen = Optional.ofNullable((Token)getLeftParen()).orElse(Token.createDummy());
		final Token rightParen = Optional.ofNullable((Token)getRightParen()).orElse(Token.createDummy());
		final String row = variables
				.stream()
				.map(VariableToken::toString)
				.collect(Collectors.joining(Separator.COMMA + " "));
		return leftParen.toString() + row + rightParen.toString();
	}


	public Paren getLeftParen() {
		return leftParen;
	}

	public Variables setLeftParen(final Paren leftParen) {
		if(leftParen.isLeftParen()) {
			this.leftParen = leftParen;
		} else {
			throw new NodeTypeException("not left paren token: " + leftParen.getName());
		}
		return this;
	}

	public List<VariableToken> getVariables() {
		return variables;
	}

	public Variables setVariables(final List<VariableToken> variables) {
		if(variables != null) {
			this.variables = variables;
		} else {
			throw new NodeTypeException("don't allow null variable");
		}
		return this;
	}

	public Paren getRightParen() {
		return rightParen;
	}

	public Variables setRightParen(final Paren rightParen) {
		if(rightParen.isRightParen()) {
			this.rightParen = rightParen;
		} else {
			throw new NodeTypeException("not right paren token: " + leftParen.getName());
		}
		return this;
	}

	public boolean hasParens() {
		final boolean hasLeftParen = getLeftParen()!=null;
		final boolean hasRightParen = getRightParen()!=null;

		if(hasLeftParen && hasRightParen) {
			return true;
		} else if(!hasLeftParen && !hasRightParen) {
			return false;
		} else {
			throw new NodeTypeException("allow only full parened or no parened");
		}
	}

	public int getParameterNum() {
		return getVariables().size();
	}

	public VariableToken getParameter() {
		if(getParameterNum() == 1) {
			return getVariables().get(0);
		} else {
			throw new NodeTypeException("this method call only if parameter num = 1, but num: " + getParameterNum());
		}
	}
}

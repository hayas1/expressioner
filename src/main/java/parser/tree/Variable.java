package parser.tree;

import binding.Bindings;
import binding.VariableBinding;
import lexer.tokens.VariableToken;
import visitor.EtVisitor;

/**
 *
 * 変数 -> 変数トークン
 * @author hayas
 *
 */

public class Variable extends Factor {
	private VariableToken token;


	@Override
	public boolean accept(final EtVisitor visitor) {
		visitor.visit(this);
		return visitor.leave(this);
	}

	@Override
	public Variable setParent(final EtNode parent) {
		return (Variable) super.setParent(parent);
	}

	public Variable setChildren(final VariableToken variableToken) {
		setVariableToken(variableToken);
		return this;
	}

	@Override
	public String toString() {
		return getVariableToken().toString();
	}

	public VariableToken getVariableToken() {
		return token;
	}

	public Variable setVariableToken(final VariableToken token) {
		if(token.isVariable()) {
			this.token = token;
		} else {
			throw new NodeTypeException("not variable token: " + token);
		}
		return this;
	}

	public VariableBinding resolveBinding() {
		return (VariableBinding)Bindings.getInstance().getBinding(getVariableToken());
	}


}

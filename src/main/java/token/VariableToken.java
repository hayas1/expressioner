package token;

public class VariableToken extends Operand {

	protected VariableToken(final String name) {
		super(name);
	}

	@Override
	public String getKind() {
		return Token.VARIABLE;
	}


	@Override
	public boolean isFactorBeginnable() {
		return true;
	}

	@Override
	public boolean isVariable() {
		return true;
	}
}

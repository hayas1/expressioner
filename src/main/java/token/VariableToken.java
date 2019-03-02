package token;

public class VariableToken extends Operand {

	protected VariableToken(final String name) {
		super(name);
	}

	@Override
	public VariableToken clone() {
		return create(getName());
	}

	public static VariableToken create(final String name) {
		if(inferenceKind(name).equals(Token.VARIABLE)) {
			return (VariableToken)Token.create(name);
		} else {
			throw new TokenException("invalid operator token: " + name);
		}
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

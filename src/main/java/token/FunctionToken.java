package token;

public class FunctionToken extends Operand {

	protected FunctionToken(final String name) {
		super(name);
	}

	@Override
	public FunctionToken clone() {
		return create(getName());
	}

	public static FunctionToken create(final String name) {
		if(inferenceKind(name).equals(Token.FUNCTION)) {
			return (FunctionToken)Token.create(name);
		} else {
			throw new TokenException("invalid operator token: " + name);
		}
	}

	@Override
	public String getKind() {
		return Token.FUNCTION;
	}

	@Override
	public boolean isFactorBeginnable() {
		return true;
	}

	@Override
	public boolean isFunction() {
		return true;
	}
}

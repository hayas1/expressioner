package token;

public class FunctionToken extends Operand {

	protected FunctionToken(final String name) {
		super(name);
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

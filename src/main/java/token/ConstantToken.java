package token;

public class ConstantToken extends Operand {

	protected ConstantToken(final String name) {
		super(name);
	}

	@Override
	public String getKind() {
		return Token.CONSTANT;
	}

	@Override
	public boolean isFactorBeginnable() {
		return true;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public boolean isNumber() {
		return Token.isValidDigit(super.getName());
	}

}

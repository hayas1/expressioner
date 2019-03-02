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
	public ConstantToken clone() {
		return create(getName());
	}

	public static ConstantToken create(final String name) {
		if(inferenceKind(name).equals(Token.CONSTANT)) {
			return (ConstantToken)Token.create(name);
		} else {
			throw new TokenException("invalid operator token: " + name);
		}
	}




	@Override
	public boolean isFactorBeginnable() {
		return true;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

}

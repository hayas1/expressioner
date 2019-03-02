package token;

public class Operator extends Token {
	public static final String EQUAL = "=";
	public static final String LESS = "<";
	public static final String LESS_EQUAL = "<=";
	public static final String GREATER = ">";
	public static final String GREATER_EQUAL = ">=";
	public static final String PLUS = "+";
	public static final String MINUS = "-";
	public static final String TIMES = "*";
	public static final String DIVIDE = "/";
	public static final String POWER = "^";
	public static final String DIFFERENTIAL = "'";

	protected Operator(final String name) {
		super(name);
	}

	@Override
	public Operator clone() {
		return create(getName());
	}

	public static Operator create(final String name) {
		if(inferenceKind(name).equals(Token.OPERATOR)) {
			return (Operator)Token.create(name);
		} else {
			throw new TokenException("invalid operator token: " + name);
		}
	}

	@Override
	public String getKind() {
		return Token.OPERATOR;
	}

	@Override
	public String toString() {
		return super.getName();
	}

	@Override
	public boolean isRelationalOperator() {
		return isEqual() || isLess() || isLessEqual() || isGreater() || isGreaterEqual();
	}

	@Override
	public boolean isSignOperator() {
		return isPlus() || isMinus();
	}

	@Override
	public boolean isAdditiveOperator() {
		return isPlus() || isMinus();
	}

	@Override
	public boolean isMultiplicativeOperator() {
		return isTimes() || isDivide();
	}

	@Override
	public boolean isPower() {
		return super.getName().equals(Operator.POWER);
	}

	@Override
	public boolean isDifferential() {
		return super.getName().equals(Operator.DIFFERENTIAL);
	}


	public boolean isEqual() {
		return super.getName().equals(Operator.EQUAL);
	}

	public boolean isLess() {
		return super.getName().equals(Operator.LESS);
	}

	public boolean isLessEqual() {
		return super.getName().equals(Operator.LESS_EQUAL);
	}

	public boolean isGreater() {
		return super.getName().equals(Operator.GREATER);
	}

	public boolean isGreaterEqual() {
		return super.getName().equals(Operator.GREATER_EQUAL);
	}

	public boolean isPlus() {
		return super.getName().equals(Operator.PLUS);
	}

	public boolean isMinus() {
		return super.getName().equals(Operator.MINUS);
	}

	public boolean isTimes() {
		return super.getName().equals(Operator.TIMES);
	}

	public boolean isDivide() {
		return super.getName().equals(Operator.DIVIDE);
	}


	public Operator reverseOperator() {
		if(isPlus()) {
			return create(Operator.MINUS);
		} else if(isMinus()) {
			return create(Operator.PLUS);
		} else {
			throw new TokenException("this token is not additive operator");
		}
	}


}

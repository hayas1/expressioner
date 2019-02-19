package token;

public class Paren extends Token {
	public static final String LEFT_PARENTHESIS = "(";
	public static final String RIGHT_PARENTHESIS = ")";
	public static final String LEFT_BRACE = "{";
	public static final String RIGHT_BRACE = "}";
	public static final String LEFT_BRACKET = "[";
	public static final String RIGHT_BRACKET = "]";

	protected Paren(final String name) {
		super(name);
	}

	@Override
	public String getKind() {
		return Token.PAREN;
	}

	@Override
	public boolean isLeftParen() {
		return isLeftParenthesis() || isLeftBrace() || isLeftBracket();
	}

	@Override
	public boolean isRightParen() {
		return isRightParenthesis() || isRightBrace() || isRightBracket();
	}

	@Override
	public boolean isFactorBeginnable() {
		return isLeftParen();
	}


	public boolean isLeftParenthesis() {
		return super.getName().equals(Paren.LEFT_PARENTHESIS);
	}

	public boolean isRightParenthesis() {
		return super.getName().equals(Paren.RIGHT_PARENTHESIS);
	}

	public boolean isLeftBrace() {
		return super.getName().equals(Paren.LEFT_BRACE);
	}

	public boolean isRightBrace() {
		return super.getName().equals(Paren.RIGHT_BRACE);
	}

	public boolean isLeftBracket() {
		return super.getName().equals(Paren.LEFT_BRACKET);
	}

	public boolean isRightBracket() {
		return super.getName().equals(Paren.RIGHT_BRACKET);
	}

	public Paren createCorrespondenceParen() {
		if(isLeftParenthesis() || isRightParenthesis()) {
			return createCorrespondenceParenthesis();
		} else if(isLeftBrace() || isRightBrace()) {
			return createCorrespondenceBrace();
		} else if(isLeftBracket() || isRightBracket()) {
			return createCorrespondenceBracket();
		} else {
			throw new TokenException("undefined correspondence paren: " + getName());
		}
	}

	private Paren createCorrespondenceParenthesis() {
		if(isLeftParenthesis()) {
			return (Paren) create(Paren.RIGHT_PARENTHESIS);
		} else if(isRightParenthesis()) {
			return (Paren) create(Paren.LEFT_PARENTHESIS);
		} else {
			throw new TokenException("not parenthesis token: " + getName());
		}
	}

	private Paren createCorrespondenceBrace() {
		if(isLeftBrace()) {
			return (Paren) create(RIGHT_BRACE);
		} else if(isRightBrace()) {
			return (Paren) create(LEFT_BRACE);
		} else {
			throw new TokenException("not brace token: " + getName());
		}
	}

	private Paren createCorrespondenceBracket() {
		if(isLeftBracket()) {
			return (Paren) create(RIGHT_BRACKET);
		} else if(isRightBracket()) {
			return (Paren) create(LEFT_BRACKET);
		} else {
			throw new TokenException("not bracket token: " + getName());
		}
	}
}

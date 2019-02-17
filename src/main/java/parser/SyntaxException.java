package parser;

import token.Token;

public class SyntaxException extends RuntimeException {
	private final Token token;

	public SyntaxException(final String message, final Token token) {
		super(message);
		this.token = token;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + token.getName();
	}

	public Token getToken() {
		return this.token;
	}

}

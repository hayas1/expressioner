package lexer;

import java.util.List;
import java.util.stream.Collectors;

import token.Token;

public class Lexer {
	private final String expression;

	public Lexer(final String expression) {
		this.expression = expression;
	}

	public String getSource() {
		return expression;
	}

	public List<Token> tokenize() {
		return new RecursiveSplitter(expression).getFollows()
				.stream()
				.map(Token::create)
				.collect(Collectors.toList());
	}


}

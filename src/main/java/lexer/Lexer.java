package lexer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lexer.tokens.Token;

public class Lexer {
	private final String expression;

	public Lexer(final String expression) {
		this.expression = expression;
	}

	public String getSource() {
		return expression;
	}

	public List<Token> tokenize() {
		final String[] separatedWords = expression.split("\\s*\\b\\s*");		//word boundary
		return Arrays.stream(separatedWords)
				.filter(str -> !str.matches("^\\s*$"))
				.map(str -> separateTerm(str))
				.flatMap(List::stream)
				.map(str -> Token.create(str))
				.collect(Collectors.toList());
	}

	public List<String> separateTerm(final String term){
		final String[] numbers = term.split("(?<=\\d)(?=[a-zA-Z])");		//100xsin10z10 -> 100 xsin10 z10
		return Arrays.stream(numbers)
				.map(str -> new FactorSplitter(str).getFollows())		//100 xsin10 z10 -> 100 x sin 10 z10
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}


}

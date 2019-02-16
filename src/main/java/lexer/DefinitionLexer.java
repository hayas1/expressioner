package lexer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lexer.tokens.Controller;
import lexer.tokens.Token;

public class DefinitionLexer extends Lexer {

	public DefinitionLexer(final String definition) {
		super(definition);
	}

	@Override
	public List<Token> tokenize() {
		final String[] tokens = super.getSource().split("\\s*\\b\\s*");

		if(tokens.length>1 && Controller.isDef(tokens[0])) {
			return makeTokens(tokens);
		} else {
			throw new LexerException("not definition expression. you'll use Lexer class");
		}
	}

	private List<Token> makeTokens(final String[] tokens) {
		if(Controller.isDefVar(tokens[0])) {
			return Arrays.stream(tokens)
					.map(tok -> Token.create(tok, Token.VARIABLE))
					.collect(Collectors.toList());
		} else if(Controller.isDefCons(tokens[0])) {
			return Arrays.stream(tokens)
					.map(tok -> Token.create(tok, Token.CONSTANT))
					.collect(Collectors.toList());
		} else if(Controller.isDefFunc(tokens[0])) {
			return Arrays.stream(tokens)
					.map(tok -> Token.create(tok, Token.FUNCTION))
					.collect(Collectors.toList());
		} else {
			throw new LexerException("not definition expression. you'll use Lexer class");
		}
	}

}

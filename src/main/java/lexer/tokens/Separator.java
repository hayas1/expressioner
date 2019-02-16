package lexer.tokens;

public class Separator extends Token{
	public static final String COMMA = ",";
	public static final String POINT = ".";

	protected Separator(final String name) {
		super(name);
	}

	@Override
	public String getKind() {
		return Token.SEPARATOR;
	}

	@Override
	public String toString() {
		return super.getName();
	}

	@Override
	public boolean isComma() {
		return super.getName().equals(Separator.COMMA);
	}

	@Override
	public boolean isPoint() {
		return super.getName().equals(Separator.POINT);
	}

}

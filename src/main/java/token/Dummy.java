package token;

public class Dummy extends Token {
	public static final String DUMMY = "";

	protected Dummy() {
		super("");
	}

	@Override
	public Dummy clone() {
		return create(getName());
	}

	public static Dummy create(final String name) {
		return (Dummy)createDummy();
	}

	@Override
	public String getKind() {
		return Token.DUMMY;
	}

	@Override
	public String toString() {
		return "";
	}



}

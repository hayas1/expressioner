package token;

import lexer.TokenTable;

/**
 * @author hayas
 * トークンの名前 <br>
 * ・英字で始まる英数字から成る1文字以上 <br>
 * トークンの種類 <br>
 * ・定数 constant <br>
 * ・変数 variable <br>
 * ・関数名 fanction <br>
 * ・括弧 paren <br>
 * ・演算子 operator <br>
 * ・制御子 controller <br>
 */
public abstract class Token {
	public static final String DIGIT = "digit";
	public static final String DUMMY = "dummy";
	public static final String CONSTANT = "constant";
	public static final String VARIABLE = "variable";
	public static final String FUNCTION = "function";
	public static final String PAREN = "paren";
	public static final String OPERATOR = "operator";
	public static final String SEPARATOR = "separator";
	public static final String CONTROLLER = "controller";


	private final String name;

	protected Token(final String name) {
		this.name = name;
	}

	public abstract String getKind();

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(final Object obj) {
		return isSameToken((Token)obj);
	}

	public static Token createDefinedKind(final String name, final String kind) {
		if(CONSTANT.equals(kind)) {
			return new ConstantToken(name);
		} else if(VARIABLE.equals(kind)) {
			return new VariableToken(name);
		} else if(FUNCTION.equals(kind)) {
			return new FunctionToken(name);
		} else if(PAREN.equals(kind)) {
			return new Paren(name);
		} else if(OPERATOR.equals(kind)) {
			return new Operator(name);
		} else if(SEPARATOR.equals(kind)) {
			return new Separator(name);
		} else if(CONTROLLER.equals(kind)) {
			return new Controller(name);
		} else {
			throw new TokenException("invalid token kind:" + kind);
		}
	}

	public static Token createInference(final String name) {
		if(isValidDigit(name)) {
			return new DigitToken(name);
		} else if(isValidName(name)) {
			TokenTable.getInstance().defineInference(name);
			return createDefinedKind(name, inferenceKind(name));
		} else {
			throw new TokenException("invalid token name: " + name);
		}
	}

	public static String inferenceKind(final String name) {
		return TokenTable.getInstance().getKind(getRemoveSuffix(name));
	}

	/**
	 * トークンを作成する。トークンが定義されていない場合は名前から種類を推測してトークンを作成する。
	 * @param name 名前
	 * @return 作成されたトークン
	 */
	public static Token create(final String name) {
		final TokenTable tokenTable = TokenTable.getInstance();
		if(tokenTable.isDefinedToken(name)) {
			return createDefinedKind(name, tokenTable.getKind(name));
		} else if(isValidDigit(name)||isValidName(name)) {
			return createInference(name);
		} else {
			throw new TokenException("cannot create token, invalid name: " + name);
		}
	}

	/**
	 * トークンを作成する。トークンが定義されていない場合は指定された種類でトークンを作成する。
	 * @param name 名前
	 * @param kind トークンが定義されていなかった場合の種類
	 * @return 作成されたトークン
	 */
	public static Token create(final String name, final String kind) {
		final TokenTable tokenTable = TokenTable.getInstance();
		if(tokenTable.isDefinedToken(name)) {
			return createDefinedKind(name, tokenTable.getKind(name));
		} else if(isValidDigit(name)){
			return new DigitToken(name);
		} else if(isValidName(name)){
			TokenTable.getInstance().defineOperand(name, kind);
			return createDefinedKind(name, kind);
		} else {
			throw new TokenException("space cannot convert to token");
		}
	}



	public static String getRemoveSuffix(final String name) {
		final String[] nameDigit = name.split("(?<=\\D)(?=\\d)");		//aiueo700 -> aiueo 700
		if(1<=nameDigit.length && nameDigit.length<=2) {
			return new String(nameDigit[0]);
		} else {
			throw new TokenException("");
		}
	}

	public static Dummy createDummy() {
		return new Dummy();
	}



	public static boolean isValidDigit(final String number) {
		return number.matches("^\\d+$");		//number(int format)
	}

	public static boolean isValidName(final String name) {
		return name.matches("^[a-zA-Z][\\w_]*$");		//alphanumeric start with alphabetic
	}

	public static boolean isValidKind(final String kind) {
		final boolean isConstant = CONSTANT.equals(kind);
		final boolean isVariable = VARIABLE.equals(kind);
		final boolean isFunction = FUNCTION.equals(kind);
		final boolean isParen = PAREN.equals(kind);
		final boolean isOperator = OPERATOR.equals(kind);
		final boolean isSeparator = SEPARATOR.equals(kind);
		final boolean isController = CONTROLLER.equals(kind);

		return isConstant || isVariable || isFunction || isParen || isOperator || isSeparator || isController;
	}


	public Token clone() {
		return create(getName());
	}

	public boolean isSameToken(final Token token) {
		return this.getKind().equals(token.getKind()) && this.getName().equals(token.getKind());
	}


	public boolean isRelationalOperator() {
		return false;
	}

	public boolean isSignOperator() {
		return false;
	}

	public boolean isAdditiveOperator() {
		return false;
	}

	public boolean isMultiplicativeOperator() {
		return false;
	}

	public boolean isLeftParen() {
		return false;
	}

	public boolean isRightParen() {
		return false;
	}

	public boolean isComma() {
		return false;
	}

	public boolean isPoint() {
		return false;
	}

	public boolean isFactorBeginnable() {
		return false;
	}

	public boolean isVariable() {
		return false;
	}

	public boolean isConstant() {
		return false;
	}

	public boolean isDigit() {
		return false;
	}

	public boolean isFunction() {
		return false;
	}

	public boolean isDifferential() {
		return false;
	}

	public boolean isPower() {
		return false;
	}

	public boolean isDef() {
		return false;
	}

	public boolean isNew() {
		return false;
	}

	public boolean isColon() {
		return false;
	}



}

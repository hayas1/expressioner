package token;

import java.util.Arrays;
import java.util.Iterator;

import binding.CalculateException;
import binding.DivideResult;

public class DigitToken extends Token {

	protected DigitToken(final String name) {
		super(name);
	}

	@Override
	public String getKind() {
		return Token.DIGIT;
	}

	@Override
	public boolean isDigit() {
		return true;
	}

	@Override
	public DigitToken clone() {
		return create(getName());
	}

	public static DigitToken create(final int integer) {
		return create(Integer.toString(integer));
	}

	public static DigitToken create(final String name) {
		if(Token.isValidDigit(name)) {
			return new DigitToken(name);
		} else {
			throw new TokenException("only digit string can convert to DigitToken: " + name);
		}

	}

	public int toInteger() {
		return Integer.parseInt(getName());
	}

	public int numberOfDigit() {
		return getName().length();
	}

	/**
	 * 後ろにbehindを追記したトークンを新しく作成する
	 * @param behinde 追記する内容
	 * @return 追記して新しく作成したトークン
	 */
	public DigitToken append(final DigitToken behind) {
		return (DigitToken)Token.create(this.getName() + behind.getName());
	}

	/**
	 * 後ろにbehindを追記したトークンを新しく作成する
	 * @param digit 追記する内容 数字でないとTokenExceptionを投げる
	 * @return 追記して新しく作成したトークン
	 */
	public DigitToken append(final String digit) {
		if(Token.isValidDigit(digit)) {
			return (DigitToken)Token.create(this.getName() + digit);
		} else {
			throw new TokenException("DigitToken cannot append no digit string: " + digit);
		}
	}

	public String pudding(final int puddingNum) {
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i<puddingNum; i++) {
			builder.append(0);
		}
		return builder.append(getName()).toString();
	}

	public String behindPudding(final int puddingNum) {
		final StringBuilder builder = new StringBuilder(getName());
		for(int i = 0; i<puddingNum; i++) {
			builder.append(0);
		}
		return builder.toString();
	}

	public DigitToken plus(final int integer) {
		return DigitToken.create(toInteger() + integer);
	}

	public DigitToken minus(final int integer) {
		return DigitToken.create(toInteger() - integer);
	}

	public DigitToken times(final int integer) {
		return DigitToken.create(toInteger() * integer);
	}

	public DigitToken divide(final int integer) {
		final DivideResult ans = divide(this, create(integer), 0);
		if(ans.hasRemainder() || ans.hasQuotientDecimal()) {
			throw new CalculateException("\"DigitToken divide int\" must be closed int: " + ans.toString());
		} else {
			return ans.getQuotientInteger();
		}
	}

	public static DigitToken plus(final DigitToken a, final DigitToken b) {
		return a.plus(b.toInteger());
	}

	public static DigitToken minus(final DigitToken a, final DigitToken b) {
		return a.minus(b.toInteger());
	}

	public static DigitToken times(final DigitToken a, final DigitToken b) {
		return a.times(b.toInteger());
	}

	/**
	 * 整数値の割り算を行う。引数depthは商の小数点以下の位の数である。
	 * @param a a/bを行うa
	 * @param b a/bを行うb
	 * @param depth 小数点以下何位まで章を求めるか
	 * @return 商と余りを整数部と小数部に分けて保存しているクラス
	 */
	public static DivideResult divide(final DigitToken a, final DigitToken b, final int depth) {
		final Iterator<String> iterator = Arrays.asList(a.behindPudding(depth).split("")).iterator();
		final StringBuilder quotient = new StringBuilder();
		StringBuilder tmpA = new StringBuilder();

		while(iterator.hasNext()) {
			tmpA = new StringBuilder(tmpA.append(iterator.next()));
			quotient.append(Integer.parseInt(tmpA.toString()) / b.toInteger());
			tmpA = new StringBuilder(Integer.toString(Integer.parseInt(tmpA.toString()) % b.toInteger()));
		}

		return new DivideResult(quotient.toString(), zeroPudding(tmpA.toString(), quotient.length()), depth);
	}

	public static String zeroPudding(final String string, final int length) {
		return String.format("%0" + length + "d", Integer.parseInt(string));
	}

	public static int gcd(final DigitToken a, final DigitToken b) {
		return euclideanGcd(a.toInteger(), b.toInteger());
	}

	public static int lcm(final DigitToken a, final DigitToken b) {
		return a.toInteger() * b.toInteger() / gcd(a, b);
	}

	public static int euclideanGcd(final int a, final int b) {
		if(b > 0) {
			return euclideanGcd(b, a%b);
		} else {
			return a;
		}
	}

}

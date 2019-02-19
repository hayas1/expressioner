package token;

import java.util.Arrays;
import java.util.Iterator;

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

	public int toInteger() {
		return Integer.parseInt(getName());
	}

	public int numberOfDigit() {
		return getName().length();
	}

//	public String puddingTo(final DigitToken target) {
//		final int thisLength = this.numberOfDigit();
//		final int targetLength = target.numberOfDigit();
//		if(thisLength >= targetLength) {
//			return this.getName();
//		} else {
//			return String.format("%0" + targetLength + "d", Integer.parseInt(this.getName()));
//		}
//	}

	public static DigitToken plus(final DigitToken a, final DigitToken b) {
		return (DigitToken)Token.create(Integer.toString(a.toInteger() + b.toInteger()));
	}

	public static DigitToken minus(final DigitToken a, final DigitToken b) {
		return (DigitToken)Token.create(Integer.toString(a.toInteger() - b.toInteger()));
	}

	public static DigitToken times(final DigitToken a, final DigitToken b) {
		return (DigitToken)Token.create(Integer.toString(a.toInteger() * b.toInteger()));
	}


	/**
	 * 整数値の割り算を行う。引数depthは商の小数点以下の位の数である。
	 * @param a a/bを行うa
	 * @param b a/bを行うb
	 * @param depth 小数点以下何位まで章を求めるか
	 * @return 商と余りを整数部と小数部に分けて保存しているクラス
	 */
	public static DivideResult divide(final DigitToken a, final DigitToken b, final int depth) {
		final Iterator<String> iterator = a.puddingIterator(depth);
		final StringBuilder quotient = new StringBuilder();
		StringBuilder tmpA = new StringBuilder();

		while(iterator.hasNext()) {
			tmpA = new StringBuilder(tmpA.append(iterator.next()));
			quotient.append(Integer.parseInt(tmpA.toString()) / b.toInteger());
			tmpA = new StringBuilder(Integer.toString(Integer.parseInt(tmpA.toString()) % b.toInteger()));
		}

		return new DivideResult(quotient.toString(), zeroPudding(tmpA.toString(), quotient.length()), depth);
	}

	public Iterator<String> puddingIterator(final int puddingNum) {
		final StringBuilder builder = new StringBuilder(getName());
		for(int i = 0; i<puddingNum; i++) {
			builder.append(0);
		}

		return Arrays.asList(builder.toString().split("")).iterator();
	}

	public static String zeroPudding(final String string, final int length) {
		return String.format("%0" + length + "d", Integer.parseInt(string));
	}

}

package binding;

import token.DigitToken;
import token.Separator;
import token.Token;

/**
 * このクラスは割り算の商と余りの情報を小数で保持する。
 * つまり、商の整数部分、商の小数部分、剰余の整数部分、剰余の小数部分をDigitTokenの型で保持している。
 * @author hayas
 *
 */
public class DivideResult {
	private final DigitToken quotientInteger;		//sho-int
	private final DigitToken quotientDecimal;		//sho-decimal
	private final DigitToken remainderInteger;		//amari-int
	private final DigitToken remainderDecimal;		//amari-decimal

	public DivideResult(final String quotient, final String remainder, final int depth) {
		if(!Token.isValidDigit(quotient) || !Token.isValidDigit(remainder)) {
			throw new CalculateException("include no digit char: " + quotient + "&" + remainder);
		}
		if(quotient.length() != remainder.length()) {
			throw new CalculateException("not same length: " + quotient + "&" + remainder);
		}
		if(depth<0 || quotient.length()<depth || remainder.length()<depth) {
			throw new CalculateException("illegal point location: " + depth);
		}

		quotientInteger = initIntegerPart(quotient.substring(0, quotient.length()-depth));		//TODO 0start. 0only
		quotientDecimal = initDecimalPart(quotient.substring(quotient.length()-depth));		//TODO depth = length
		remainderInteger = initIntegerPart(remainder.substring(0, remainder.length()-depth));
		remainderDecimal = initDecimalPart(remainder.substring(remainder.length()-depth));
	}

	private DigitToken initIntegerPart(final String integerPart) {
		final String zeroRemove = integerPart.replaceFirst("^0+", "");
		if(zeroRemove.isEmpty()) {
			return (DigitToken)Token.create("0");
		} else {
			return (DigitToken)Token.create(zeroRemove);
		}
	}

	private DigitToken initDecimalPart(final String decimalPart) {
		final String zeroRemove = decimalPart.replaceFirst("0+$", "");
		if(zeroRemove.isEmpty()) {
			return null;
		} else {
			return (DigitToken)Token.create(zeroRemove);
		}
	}


	@Override
	public String toString() {
		final String quotient;
		final String remainder;
		if(hasQuotientDecimal()) {
			quotient = getQuotientInteger().toString() + Separator.POINT + getQuotientDecimal().toString();
		} else {
			quotient = getQuotientInteger().toString();
		}

		if(hasRemainderDecimal()) {
			remainder = getRemainderInteger().toString() + Separator.POINT + getRemainderDecimal().toString();
		} else {
			remainder = getRemainderInteger().toString();
		}

		if(hasRemainder()) {
			return quotient + "…" + remainder;
		} else {
			return quotient;
		}
	}


	public DigitToken getQuotientInteger() {
		return quotientInteger;
	}

	public boolean hasQuotientDecimal() {
		return getQuotientDecimal()!=null;
	}

	public DigitToken getQuotientDecimal() {
		return quotientDecimal;
	}

	public boolean hasRemainder() {
		return !getRemainderInteger().getName().matches("^0*$") || hasRemainderDecimal();
	}

	public DigitToken getRemainderInteger() {
		return remainderInteger;
	}

	public boolean hasRemainderDecimal() {
		return getRemainderDecimal()!=null;
	}

	public DigitToken getRemainderDecimal() {
		return remainderDecimal;
	}



}

package binding;

import token.DigitToken;
import token.Separator;
import token.Token;
import tree.DigitConstant;

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
		} else if(quotient.length() != remainder.length()) {
			throw new CalculateException("not same length: " + quotient + "&" + remainder);
		}

		DigitConstant quotientConstant = DigitConstant.makeNode((DigitToken)Token.create(quotient), depth);
		DigitConstant remainderConstant = DigitConstant.makeNode((DigitToken)Token.create(remainder), depth);
		quotientInteger = initIntegerPart(quotientConstant.getInteger().getName());
		quotientDecimal = initDecimalPart(quotientConstant.hasDecimal()? quotientConstant.getDecimal().getName(): "");
		remainderInteger = initIntegerPart(remainderConstant.getInteger().getName());
		remainderDecimal = initDecimalPart(remainderConstant.hasDecimal()? remainderConstant.getDecimal().getName(): "");
	}

	public DivideResult(final DigitToken qInt, final DigitToken qDec, final DigitToken rInt, final DigitToken rDec) {
		quotientInteger = initIntegerPart(qInt.getName());
		quotientDecimal = qDec!=null? initDecimalPart(qDec.getName()): null;
		remainderInteger = initIntegerPart(rInt.getName());
		remainderDecimal = rDec!=null? initDecimalPart(rDec.getName()): null;
	}

	private DigitToken initIntegerPart(final String integerPart) {
		final String zeroRemove = integerPart.replaceFirst("^0+", "");
		if(zeroRemove.isEmpty()) {
			return DigitToken.create(0);
		} else {
			return DigitToken.create(zeroRemove);
		}
	}

	private DigitToken initDecimalPart(final String decimalPart) {
		final String zeroRemove = decimalPart.replaceFirst("0+$", "");
		if(zeroRemove.isEmpty()) {
			return null;
		} else {
			return DigitToken.create(zeroRemove);
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

	/**
	 * 余りの小数点を左にlenthだけずらしたDivideResultを新しく作成する
	 * @param shift
	 * @return 新しく作成したDivideResult
	 */
	public DivideResult shiftRemainder(final int shift) {
		final int depth;
		final DigitConstant digits;
		if(hasRemainderDecimal()) {
			depth = shift + getRemainderDecimal().numberOfDigit();
			digits = DigitConstant.makeNode(getRemainderInteger().append(getQuotientDecimal()),depth);
		} else {
			depth = shift;
			digits = DigitConstant.makeNode(getRemainderInteger(),depth);
		}

		return new DivideResult(getQuotientInteger(), getQuotientDecimal(), digits.getInteger(), digits.getDecimal());
	}

	public DigitConstant getQuotient() {
		if(hasQuotientDecimal()) {
			return new DigitConstant().setChildren(getQuotientInteger(), Separator.create(Separator.POINT), getQuotientDecimal());
		} else {
			return new DigitConstant().setChildren(getQuotientInteger(), null, null);
		}
	}



}

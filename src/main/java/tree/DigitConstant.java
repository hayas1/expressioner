package tree;

import java.util.Collections;

import parser.EtParser;
import token.DigitToken;
import token.Separator;
import token.Token;
import visitor.EtVisitor;

/**
 *
 * 数字定数 -> 数字[.数字]
 * @author hayas
 *
 */

public class DigitConstant extends Constant {
	private DigitToken integer;
	private Separator point;
	private DigitToken decimal;


	@Override
	public boolean accept(final EtVisitor visitor) {
		visitor.visit(this);
		return visitor.leave(this);
	}

	@Override
	public DigitConstant setParent(final EtNode parent) {
		return (DigitConstant)super.setParent(parent);
	}

	public DigitConstant setChildren(final DigitToken integer, final Separator point, final DigitToken decimal) {
		setInteger(integer);
		setPoint(point);
		setDecimal(decimal);

		return this;
	}

	@Override
	public String toString() {
		if(hasDecimal()) {
			return getInteger().toString() + getPoint().toString() + getDecimal().toString();
		} else {
			return getInteger().toString();
		}
	}


	public DigitToken getInteger() {
		return integer;
	}

	public DigitConstant setInteger(final DigitToken integer) {
		if(integer!=null && integer.isDigit()) {
			this.integer = integer;
		} else {
			throw new NodeTypeException("not integer token: " + integer.getName());
		}
		return this;
	}

	public boolean hasDecimal() {
		final boolean hasPoint = getPoint()!=null;
		final boolean hasDecimal = getDecimal()!=null;
		if(hasPoint && !hasDecimal) {
			throw new NodeTypeException("invalid number type: integer point null");
		} else if(!hasPoint && hasDecimal) {
			throw new NodeTypeException("invalid number type: integer null decimal");
		} else {
			return hasDecimal;
		}
	}

	public Separator getPoint() {
		return point;
	}

	public DigitConstant setPoint(final Separator point) {
		if(point==null || point.isPoint()) {
			this.point = point;
		} else {
			throw new NodeTypeException("not point token: " + point.getName());
		}
		return this;
	}

	public DigitToken getDecimal() {
		return decimal;
	}

	//TODO only length approximately 3 can convert mutually double
	public DigitConstant setDecimal(final DigitToken decimal) {
		if(decimal==null || decimal.isDigit()) {
			this.decimal = decimal;
		} else {
			throw new NodeTypeException("not integer token: " + decimal.getName());
		}
		return this;
	}


	public int toInteger() {
		if(hasDecimal()) {
			return Integer.parseInt(getInteger().getName());
		} else {
			throw new NodeTypeException("not integer token: " + toString());
		}
	}

	public double toDouble() {
		return Double.parseDouble(getInteger().getName() + getPoint().getName() + getDecimal().getName());
	}

	/**
	 * 引数の整数をもとに親が未設定のノードを作成
	 * @param integer 作成するノードの値
	 * @return 作成したノード
	 */
	public static DigitConstant makeNode(final int integer) {
		return new DigitConstant().setInteger((DigitToken)Token.create(Integer.toString(integer)));
	}

	public static int gcd(final DigitConstant a, final DigitConstant b) {
		return euclideanGcd(a.toInteger(), b.toInteger());
	}

	public static int lcm(final DigitConstant a, final DigitConstant b) {
		return a.toInteger() * b.toInteger() / gcd(a, b);
	}

	public static int euclideanGcd(final int a, final int b) {
		if(b > 0) {
			return euclideanGcd(b, a%b);
		} else {
			return a;
		}
	}

	public static DigitConstant plus(final DigitConstant a, final DigitConstant b) {
		final String aInteger = a.getInteger().getName();
	}


	public Term convertToFraction() {
		if(!hasDecimal()) {
			return new EtParser(Collections.singletonList(getInteger())).createTermEt();
		} else {
			return devide(a, b);
		}
	}

	public static Term devide(final DigitConstant a, final DigitConstant b) {

	}


}

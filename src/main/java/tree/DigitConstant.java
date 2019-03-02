package tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import binding.DivideResult;
import parser.EtParser;
import token.DigitToken;
import token.Operator;
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

	@Override
	public DigitConstant copySubEt(final EtNode parent) {
		final DigitConstant digitConstant = new DigitConstant();

		final DigitToken integer = getInteger().clone();
		final Separator point = hasDecimal()? getPoint(): null;
		final DigitToken decimal = hasDecimal()? getDecimal().clone(): null;

		return digitConstant.setParent(parent).setChildren(integer, point, decimal);
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

	public int decimalPlace() {
		return hasDecimal()? getDecimal().numberOfDigit(): 0;
	}

	/**
	 * 整数部分と小数部分を連結した新たなDigitTokenを作成する。小数点以下は引数で指定した桁になるまで0でパディングする
	 * @param depth 小数点以下の桁数
	 * @return 連結したDigitToken
	 */
	public DigitToken decimalConcat(final int depth) {
		if(!hasDecimal()) {
			return getInteger();
		} else if(decimalPlace()>=depth) {
			return getInteger().append(getDecimal());
		} else {
			return getInteger().append(getDecimal().behindPudding(depth-decimalPlace()));
		}
	}

	/**
	 * 引数の整数をもとに親が未設定のノードを作成
	 * @param integer 作成するノードの値
	 * @return 作成したノード(親未設定)
	 */
	public static DigitConstant makeNode(final int integer) {
		return new DigitConstant().setInteger(DigitToken.create(integer));
	}

	/**
	 * 引数のDigitTokenを小数点以下depth桁として解釈したDigitConstantのノードを作成。親は未設定。
	 * depthが負だとNodeTypeExceptionを投げる。
	 * @param digit 数字列
	 * @param depth 小数点以下の桁数
	 * @return 作成したノード(親未設定)
	 */
	public static DigitConstant makeNode(final DigitToken digit, final int depth) {
		final DigitToken integer, decimal;
		if(depth<0) {
			throw new NodeTypeException(digit.toString() + " cannot convert to depth " + depth + " decimal");
		} else if(depth == 0) {
			integer = DigitToken.create(digit.getName().substring(0, digit.numberOfDigit()));
			decimal = null;
		} else if(digit.numberOfDigit() > depth) {
			integer = DigitToken.create(digit.getName().substring(0, digit.numberOfDigit()-depth));
			decimal = DigitToken.create(digit.getName().substring(digit.numberOfDigit()-depth));
		} else if(digit.numberOfDigit() <= depth){
			integer = DigitToken.create("0");
			decimal = DigitToken.create(digit.pudding(depth-digit.numberOfDigit()));
		} else {
			throw new NodeTypeException("fatal error");
		}

		if(decimal == null) {
			return DigitConstant.makeNode(integer.toInteger());
		} else {
			return new DigitConstant().setChildren(integer, (Separator)Token.create(Separator.POINT), decimal);
		}
	}

	/**
	 * 加算を行い、木を作り替える
	 * つまり、このDigitConstantと引数のDigitConstantで加算を行った結果得たDigitConstantノードでこのDigitConstantを置き換える
	 * @param digit 親が未設定のDigitConstant
	 * @return 加算を行った結果得たノード
	 */
	public DigitConstant plus(final DigitConstant digit) {
		if(digit.getParent()!=null) {
			throw new NodeTypeException("plus operand digit must have null parent");
		}

		final int depth = Math.max(this.decimalPlace(), digit.decimalPlace());
		final DigitToken ans = DigitToken.plus(this.decimalConcat(depth), digit.decimalConcat(depth));
		return (DigitConstant)this.replace(makeNode(ans, depth));
	}

	/**
	 * 減算を行い、木を作り替える
	 * つまり、このDigitConstantと引数のDigitConstantで減算を行った結果得たDigitConstantノードでこのDigitConstantを置き換える
	 * @param digit 親が未設定のDigitConstant
	 * @return 減算を行った結果得たノード
	 */
	public DigitConstant minus(final DigitConstant digit) {
		if(digit.getParent()!=null) {
			throw new NodeTypeException("minus operand digit must have null parent");
		}

		final int depth = Math.max(this.decimalPlace(), digit.decimalPlace());
		final DigitToken ans = DigitToken.minus(this.decimalConcat(depth), digit.decimalConcat(depth));
		return (DigitConstant)this.replace(makeNode(ans, depth));
	}

	/**
	 * 乗算を行い、木を作り替える
	 * つまり、このDigitConstantと引数のDigitConstantで乗算を行った結果得たDigitConstantノードでこのDigitConstantを置き換える
	 * @param digit 親が未設定のDigitConstant
	 * @return 乗算を行った結果得たノード
	 */
	public DigitConstant times(final DigitConstant digit) {
		if(digit.getParent()!=null) {
			throw new NodeTypeException("times operand digit must have null parent");
		}

		final DigitToken ans = DigitToken.times(this.decimalConcat(0), digit.decimalConcat(0));
		return (DigitConstant)this.replace(makeNode(ans, this.decimalPlace() + digit.decimalPlace()));
	}


	/**
	 * depthで与えられた位までの除算を行い、結果に応じて木を作り替える<br>
	 * ・余りが出なかった場合 このDigitConstantを除算結果の小数の商を表すDigitConstantで置き換える<br>
	 * ・余りが出た場合 このDigitConstantの親であるTermの子に分母として引数のDigitConstantを挿入する<br>
	 * @param digit 親が未設定のDigitConstant
	 * @param depth 商を小数点以下この値の桁まで計算する
	 * @return 余りが出なかった場合は入れ替えたノード、余りが出た場合はTermのノード
	 */
	public EtNode divide(final DigitConstant digit, final int depth) {
		if(digit.getParent()!=null) {
			throw new NodeTypeException("divide operand digit must have null parent");
		}

		final int digitDepth = digit.decimalPlace();
		final DigitToken thisConcatted = this.decimalConcat(digitDepth);
		final DigitToken digitConcatted = digit.decimalConcat(digitDepth);
		final DivideResult ans = DigitToken.divide(thisConcatted, digitConcatted, depth+digitDepth).shiftRemainder(digitDepth);

		if(!ans.hasRemainder()) {
			return this.replace(ans.getQuotient());
		} else {
			
		}
	}


	public Term toFraction() {		//TODO Term may implement reduction operation
		if(!hasDecimal()) {
			return new EtParser(Collections.singletonList(getInteger())).createTermEt();
		} else {
			final DigitToken numerator = decimalConcat(0);
			final DigitToken denominator = DigitToken.create((int)Math.pow(10, decimalPlace()));
			final int gcd = DigitToken.gcd(numerator, denominator);

			final List<Token> term = Arrays.asList(numerator.divide(gcd), Token.create(Operator.DIVIDE), denominator.divide(gcd));
			return (Term)this.replace(new EtParser(term).createTermEt());
		}
	}



}

package tree;

import lexer.tokens.ConstantToken;
import lexer.tokens.Separator;
import visitor.EtVisitor;

/**
 *
 * 数字定数 -> 数字[.数字]
 * @author hayas
 *
 */

public class NumberConstant extends Constant {
	private ConstantToken integer;
	private Separator point;
	private ConstantToken decimal;


	@Override
	public boolean accept(final EtVisitor visitor) {
		visitor.visit(this);
		return visitor.leave(this);
	}

	@Override
	public NumberConstant setParent(final EtNode parent) {
		return (NumberConstant)super.setParent(parent);
	}

	public NumberConstant setChildren(final ConstantToken integer, final Separator point, final ConstantToken decimal) {
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


	public ConstantToken getInteger() {
		return integer;
	}

	public void setInteger(final ConstantToken integer) {
		if(integer.isConstant()) {
			this.integer = integer;
		} else {
			throw new NodeTypeException("not integer token: " + integer.getName());
		}
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

	public NumberConstant setPoint(final Separator point) {
		if(point==null || point.isPoint()) {
			this.point = point;
		} else {
			throw new NodeTypeException("not point token: " + point.getName());
		}
		return this;
	}

	public ConstantToken getDecimal() {
		return decimal;
	}

	public NumberConstant setDecimal(final ConstantToken decimal) {
		if(decimal==null || decimal.isConstant()) {
			this.decimal = decimal;
		} else {
			throw new NodeTypeException("not integer token: " + decimal.getName());
		}
		return this;
	}



}

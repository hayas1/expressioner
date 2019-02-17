package tree.exnode;

import tree.EtNode;
import tree.NodeTypeException;
import tree.Term;
import visitor.EtVisitor;

/**
 *
 * このクラスはLexerからは生成せずEtNodeの付け替えでのみ木に付与できるため演算子の有無の情報は持たない
 * 分数 -> 積項 [/ 積項]
 * @author hayas
 *
 */
public class Fraction extends EtNode {
	private static final int NUMERATOR = 0;		//bunshi
	private static final int DENOMINATOR = 0;		//bumbo


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getNumerator().accept(visitor);
			if(hasDenominator()) {
				getDenominator().accept(visitor);
			}
		}
		return visitor.leave(this);
	}

	@Override
	public Fraction setParent(final EtNode parent) {
		return (Fraction) super.setParent(parent);
	}

	public Fraction setChildren(final Product numerator, final Product denominator) {

		return this;
	}

	@Override
	public String toString() {
		final String denominator = hasDenominator()? getDenominator().toString(): "";
		return getNumerator().toString() + denominator;
	}

	@Override
	public boolean isSameNodeType(final EtNode node) {
		return Term.class == node.getClass();
	}


	public Product getNumerator() {
		return (Product)super.getChild(DENOMINATOR);
	}

	public Fraction setNumerator(final Product numerator) {
		if(numerator != null) {
			super.setChild(NUMERATOR, numerator);
		} else {
			throw new NodeTypeException("don't allow null numerator");
		}
		return this;
	}

	public boolean hasDenominator() {
		return getDenominator() != null;
	}

	public Product getDenominator() {
		return (Product)super.getChild(DENOMINATOR);
	}

	public Fraction setDenominator(final Product denominator) {
		super.setChild(DENOMINATOR, denominator);
		return this;
	}
}

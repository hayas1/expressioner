package tree;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import token.Dummy;
import token.Operator;
import token.Token;
import visitor.EtVisitor;

/**
 *
 * 式 -> [符号] 項 {加法演算子 項}
 * @author hayas
 *
 */
public class Expression extends EtNode {
	private static final int TERM = 0;
	private static final int TERMS_BEGIN = 1;
	private int termsSize = 0;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getTerm().accept(visitor);
			if(hasTerms()) {
				getTerms().forEach(ter -> ter.accept(visitor));
			}
		}

		return visitor.leave(this);
	}

	@Override
	public Expression setParent(final EtNode parent) {
		return (Expression)super.setParent(parent);
	}

	public Expression setChildren(final Operator sign, final Term term, final List<Term> terms) {
		setTerm(term);
		setSign(sign);		//this call getTerm()
		setTerms(terms);

		return this;
	}

	@Override
	public String toString() {
		if(!hasTerms()) {
			return (Optional.ofNullable((Token)getSign()).orElse(Token.createDummy())).toString() + getTerm().toString();
		} else {
			final String term = getTerm().toString();
			final String terms = getTerms()
					.stream()
					.map(ter -> ter.getAdditiveOperator().toString() + ter.toString())
					.collect(Collectors.joining());
			return (Optional.ofNullable((Token)getSign()).orElse(Token.createDummy())).toString() + term + terms;
		}
	}

	@Override
	public Expression copySubEt(final EtNode parent) {
		final Expression expression = new Expression();

		final Operator sign = hasSign()? getSign(): null;
		final Term term = getTerm().copySubEt(expression);
		final List<Term> terms;
		if(!hasTerms()) {
			terms = Collections.emptyList();
		} else {
			terms = getTerms()
					.stream()
					.map(ter -> ter.copySubEt(expression))
					.collect(Collectors.toList());
		}

		return expression.setParent(parent).setChildren(sign, term, terms);
	}

	public String getSignString() {
		if(hasSign()) {
			return getSign().toString();
		} else {
			return Dummy.DUMMY;
		}
	}

	public boolean hasSign() {
		return getSign() != null;
	}

	/**
	 * この式の先頭の符号を取得するが、それは先頭の項がもつ加算演算子と同じである
	 * つまり、このメソッドはgetTerm.getAdditiveOperator()と同様
	 * @return この式の先頭の符号
	 */
	public Operator getSign() {
		return getTerm().getAdditiveOperator();
	}

	/**
	 * この式の先頭の符号を取得するが、それは先頭の項がもつ加算演算子と同じである
	 * つまり、このメソッドはgetTerm.setAdditiveOperator(Operator)と同様
	 * @param sign 式の先頭の符号
	 * @return この式ノード
	 */
	public Expression setSign(final Operator sign) {
		getTerm().setAdditiveOperator(sign);
		return this;
	}

	public Term getTerm() {
		return (Term)super.getChild(TERM);
	}

	public Expression setTerm(final Term term) {
		if(term != null) {
			super.setChild(TERM, term);
		} else {
			throw new NodeTypeException("don't allow null term");
		}
		return this;
	}

	public boolean hasTerms() {
		return termsSize != 0;
	}

	public List<Term> getTerms() {
		return super.getChildren(TERMS_BEGIN, termsSize).downCast(Term.class);
	}

	public Expression setTerms(final List<Term> terms) {
		final List<Term> list = Optional.ofNullable(terms).orElse(Collections.emptyList());
		termsSize = list.size();
		super.setChildren(TERMS_BEGIN, list);
		return this;
	}


}

package tree;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import token.Dummy;
import token.Operator;
import visitor.EtVisitor;

/**
 * @author hayas
 * 項 = 累乗因子 {[乗法演算子] 累乗因子}
 *
 */
public class Term extends EtNode {
	private static final int POWER_FACTOR = 0;
	private Operator additiveOperator;
	private static final int POWER_FACTORS_BEGIN = 1;
	private int powerFactorsSize = 0;

	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getPowerFactor().accept(visitor);
			if(hasPowerFactors()) {
				getPowerFactors().forEach(pow -> pow.accept(visitor));
			}
		}
		return visitor.leave(this);
	}

	@Override
	public Term setParent(final EtNode parent) {
		return (Term) super.setParent(parent);
	}

	public Term setChildren(final PowerFactor powerFactor, final List<PowerFactor> powerFactors) {
		setPowerFactor(powerFactor);
		setPowerFactors(powerFactors);

		return this;
	}

	@Override
	public String toString() {
		if(!hasPowerFactors()) {
			return getPowerFactor().toString();
		} else {
			final String powerFactor = getPowerFactor().toString();
			final String powerFactors = getPowerFactors()
					.stream()
					.map(pow -> pow.getMultiplicativeOperatorString() + pow.toString())
					.collect(Collectors.joining());

			return powerFactor + powerFactors;
		}
	}

	@Override
	public Term copySubEt(final EtNode parent) {
		final Term term = new Term().setAdditiveOperator(getAdditiveOperator().clone());

		final PowerFactor powerFactor = getPowerFactor().copySubEt(term);
		final List<PowerFactor> powerFactors;
		if(!hasPowerFactors()) {
			powerFactors = Collections.emptyList();
		} else {
			powerFactors = getPowerFactors()
					.stream()
					.map(pow -> pow.copySubEt(term))
					.collect(Collectors.toList());
		}

		return term.setParent(parent).setChildren(powerFactor, powerFactors);
	}

	public String getAdditiveOperatorString() {
		if(hasAdditiveOperator()) {
			return getAdditiveOperator().toString();
		} else {
			return Dummy.DUMMY;
		}
	}

	public boolean hasAdditiveOperator() {
		return getAdditiveOperator() != null;
	}

	public Operator getAdditiveOperator() {
		return additiveOperator;
	}

	public Term setAdditiveOperator(final Operator additiveOperator) {
		if(additiveOperator==null || additiveOperator.isAdditiveOperator()) {
			this.additiveOperator = additiveOperator;
		} else {
			throw new NodeTypeException("it s not additive operator: " + additiveOperator.toString());
		}
		return this;
	}

	public PowerFactor getPowerFactor() {
		return (PowerFactor)super.getChild(POWER_FACTOR);
	}

	public Term setPowerFactor(final PowerFactor powerFactor) {
		if(powerFactor != null) {
			super.setChild(POWER_FACTOR, powerFactor);
		} else {
			throw new NodeTypeException("don't allow null factor");
		}
		return this;
	}

	public boolean hasPowerFactors() {
		return powerFactorsSize != 0;
	}

	public List<PowerFactor> getPowerFactors() {
		return super.getChildren(POWER_FACTORS_BEGIN, powerFactorsSize).downCast(PowerFactor.class);
	}

	public Term setPowerFactors(final List<PowerFactor> powerFactors) {
		final List<PowerFactor> list = Optional.ofNullable(powerFactors).orElse(Collections.emptyList());
		powerFactorsSize = list.size();
		super.setChildren(POWER_FACTORS_BEGIN, list);
		return this;
	}



}

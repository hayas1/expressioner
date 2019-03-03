package tree;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import token.Operator;
import visitor.EtVisitor;

/**
 *
 * 累乗因子 -> 因子 {累乗演算子 累乗因子}
 * @author hayas
 *
 */
public class PowerFactor extends Argument {
	private static final int FACTOR = 0;
	private Operator multiplicativeOperator;
	private static final int FACTORS_BEGIN = 1;

	private int factorsSize = 0;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getFactor().accept(visitor);
			if(hasFactors()) {
				getFactors().forEach(fac -> fac.accept(visitor));
			}
		}

		return visitor.visit(this);
	}

	@Override
	public PowerFactor setParent(final EtNode parent) {
		return (PowerFactor) super.setParent(parent);
	}

	public PowerFactor setChildren(final Factor factor, final List<Factor> factors) {
		setFactor(factor);
		setFactors(factors);

		return this;
	}

	@Override
	public String toString() {
		if(!hasFactors()) {
			return getFactor().toString();
		} else {
			final String factor = getFactor().toString();
			final String factors = getFactors()
					.stream()
					.map(fac -> fac.getPowerOperatorString() + fac.toString())
					.collect(Collectors.joining());
			return factor + factors;
		}
	}

	@Override
	public PowerFactor copySubEt(final EtNode parent) {
		final PowerFactor powerFactor = new PowerFactor();

		final Factor factor = getFactor().copySubEt(powerFactor);
		final List<Factor> factors;
		if(!hasFactors()) {
			factors = Collections.emptyList();
		} else {
			factors = getFactors()
					.stream()
					.map(fac -> fac.copySubEt(powerFactor))
					.collect(Collectors.toList());
		}

		return powerFactor.setParent(parent).setChildren(factor, factors);
	}

	public Factor getFactor() {
		return (Factor)super.getChild(FACTOR);
	}

	public PowerFactor setFactor(final Factor factor) {
		if(factor != null) {
			super.setChild(FACTOR, factor);
		} else {
			throw new NodeTypeException("don't allow null factor");
		}
		return this;
	}

	public String getMultiplicativeOperatorString() {
		if(hasMultiplicativeOperator()) {
			return getMultiplicativeOperator().toString();
		} else {
			return Operator.TIMES;
		}
	}

	public boolean hasMultiplicativeOperator() {
		return getMultiplicativeOperator() != null;
	}

	public Operator getMultiplicativeOperator() {
		return multiplicativeOperator;
	}

	public PowerFactor setMultiplicativeOperator(final Operator multiplicativeOperator) {
		if(multiplicativeOperator==null || multiplicativeOperator.isMultiplicativeOperator()) {
			this.multiplicativeOperator = multiplicativeOperator;
		} else {
			throw new NodeTypeException("it s not multiplicative operator: " + multiplicativeOperator.toString());
		}
		return this;
	}

	public boolean hasFactors() {
		return factorsSize != 0;
	}

	public List<Factor> getFactors() {
		return super.getChildren(FACTORS_BEGIN, factorsSize).downCast(Factor.class);
	}

	public PowerFactor setFactors(List<Factor> factors) {
		final List<Factor> list = Optional.ofNullable(factors).orElse(Collections.emptyList());
		factorsSize = list.size();
		super.setChildren(FACTORS_BEGIN, list);
		return this;
	}


	/**
	 * このノードがnodeTypeA^nodeTypeBの形であるかどうかを判定する。
	 * @param nodeTypeA 基数の型
	 * @param nodeTypeB 指数の型
	 * @return 判定結果
	 */
//	public boolean isFactorPowFacor(final Factor nodeTypeA, final Factor nodeTypeB) {
//		if(hasOperator()) {
//			return getFactor().isSameNodeType(nodeTypeA) && getFactors().isSameNodeType(nodeTypeB);
//		} else {
//			return false;
//		}
//	}

}

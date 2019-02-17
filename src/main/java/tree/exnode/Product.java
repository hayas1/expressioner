package tree.exnode;

import java.util.List;
import java.util.stream.Collectors;

import token.Operator;
import tree.EtNode;
import tree.NodeTypeException;
import tree.PowerFactor;
import tree.Term;
import visitor.EtVisitor;

/**
 *
 * このクラスはLexerからは生成せずEtNodeの付け替えでのみ木に付与できるため演算子の有無の情報は持たない
 * 積項 -> 累乗因子 {[*]累乗因子}
 * @author hayas
 *
 */
public class Product extends EtNode {

	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getPowerFactors().forEach(fac -> visitor.visit(fac));
		}
		return visitor.leave(this);
	}

	@Override
	public Product setParent(final EtNode parent) {
		return (Product) super.setParent(parent);
	}

	public Product setChildren(final List<PowerFactor> powerFactors) {
		setPowerFactors(powerFactors);

		return this;
	}

	@Override
	public String toString() {
		return getPowerFactors()
				.stream()
				.map(PowerFactor::toString)
				.collect(Collectors.joining(Operator.TIMES));
	}

	@Override
	public boolean isSameNodeType(final EtNode node) {
		return Term.class == node.getClass();
	}

	public List<PowerFactor> getPowerFactors(){
		return super.getChildren().downCast(PowerFactor.class);
	}

	public Product setPowerFactors(final List<PowerFactor> powerFactors) {
		if(powerFactors!=null && !powerFactors.isEmpty()) {
			super.setChildrenNodes(powerFactors.toArray(new PowerFactor[powerFactors.size()]));
		} else {
			throw new NodeTypeException("don't allow null or empty power factors");
		}
		return this;
	}
}

package visitor;

import tree.Expression;
import tree.MainExpression;
import tree.Term;

//TODO 掛け算とか全部つくってから作り直し
public class ReverseVisitor extends EtVisitor {
	private final boolean isPositive;
	private final boolean isNumerator;

	public ReverseVisitor(final boolean isPositive, final boolean isNumerator) {
		this.isPositive = isPositive;
		this.isNumerator = isNumerator;
	}

	@Override
	public boolean visit(final MainExpression node) {
		node.setSign(null);
		return super.visit(node);
	}

	@Override
	public boolean visit(final Expression node) {
		if(!isPositive) {
			node.reverseSign();
		}

		return super.visit(node);
	}

	@Override
	public boolean visit(final Term node) {
		if(!isNumerator) {

		}
		return super.visit(node);
	}

}

package visitor;

import tree.Condition;
import tree.Expression;
import tree.Expressions;
import tree.FactorExpression;
import tree.Function;
import tree.MainExpression;
import tree.NumberConstant;
import tree.PowerFactor;
import tree.Term;
import tree.Variable;
import tree.VariableConstant;
import tree.exnode.Fraction;
import tree.exnode.Product;

/**
 *
 * visitの返り値をfalseにするとそれ以下のノードを探索しない。
 * leaveの返り値はそのノードのacceptの返り値になる。
 * @author hayas
 *
 */
public abstract class EtVisitor {

	public boolean visit(final Condition node) {
		return true;
	}

	public boolean leave(final Condition node) {
		return true;
	}

	public boolean visit(final MainExpression node) {
		return true;
	}

	public boolean leave(final MainExpression node) {
		return true;
	}

	public boolean visit(final Expression node) {
		return true;
	}

	public boolean leave(final Expression node) {
		return true;
	}

	public boolean visit(final Term node) {
		return true;
	}

	public boolean leave(final Term node) {
		return true;
	}

	public boolean visit(final PowerFactor node) {
		return true;
	}

	public boolean leave(final PowerFactor node) {
		return true;
	}

	public boolean visit(final VariableConstant node) {
		return true;
	}

	public boolean leave(final VariableConstant node) {
		return true;
	}

	public boolean visit(final NumberConstant node) {
		return true;
	}

	public boolean leave(final NumberConstant node) {
		return true;
	}

	public boolean visit(final Variable node) {
		return true;
	}

	public boolean leave(final Variable node) {
		return true;
	}

	public boolean visit(final FactorExpression node) {
		return true;
	}

	public boolean leave(final FactorExpression node) {
		return true;
	}

	public boolean visit(final Function node) {
		return true;
	}

	public boolean leave(final Function node) {
		return true;
	}

	public boolean visit(final Expressions node) {
		return true;
	}

	public boolean leave(final Expressions node) {
		return true;
	}


	public boolean visit(final Product node) {
		return true;
	}

	public boolean leave(final Product node) {
		return true;
	}

	public boolean visit(final Fraction node) {
		return true;
	}

	public boolean leave(final Fraction node) {
		return true;
	}




}

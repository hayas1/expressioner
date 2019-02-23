package visitor;

import java.util.ArrayList;
import java.util.List;

import tree.PowerFactor;
import tree.Term;

/**
 * 主項を探索し、分子・分母の累乗因子をそれぞれリストアップする
 * 主項以外のノードを探索した場合の動作は未定義 式の直下の項を探索することを前提としている
 * @author hayas
 *
 */
public class TermSimplizeVisitor extends EtVisitor {
	private final List<PowerFactor> numerator = new ArrayList<>();
	private final List<PowerFactor> denomitor = new ArrayList<>();

	private boolean isNumerator = true;


	public List<PowerFactor> getNumerators() {
		return numerator;
	}

	public List<PowerFactor> getDenimitors() {
		return denomitor;
	}


	@Override
	public boolean visit(final Term node) {
		if(isNumerator) {
			numerator.add(node.getPowerFactor());
		} else {
			denomitor.add(node.getPowerFactor());
		}

		if(node.isDivide()) {
			isNumerator = !isNumerator;
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(final PowerFactor node) {
		return false;		//don't visit power factor
	}


}

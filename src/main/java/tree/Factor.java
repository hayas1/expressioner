package tree;

/**
 * 因子 -> 変数 | 定数 | 因子式 | 関数
 * @author hayas
 *
 */

public abstract class Factor extends EtNode {
	public static final String VARIABLE = "variable";
	public static final String CONSTANT = "constant";
	public static final String FACTOR_EXPRESSION = "factorExpression";
	public static final String FUNCTION = "function";

	//public abstract String getFactorType();

	@Override
	public Factor setParent(EtNode parent) {
		return (Factor) super.setParent(parent);
	}

}
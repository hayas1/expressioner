package parser.tree;

/**
 *
 * 引数 -> 累乗因子 | 式列
 * @author hayas
 *
 */
public abstract class Argument extends EtNode {

	@Override
	public Argument setParent(EtNode parent) {
		return (Argument) super.setParent(parent);
	}


}

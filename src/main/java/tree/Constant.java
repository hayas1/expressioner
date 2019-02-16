package tree;

/**
 *
 * 定数 -> 文字定数 | 数字定数
 * @author hayas
 *
 */
public abstract class Constant extends Factor {

	@Override
	public Constant setParent(EtNode parent) {
		return (Constant) super.setParent(parent);
	}
}

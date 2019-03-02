package tree;

import binding.Bindings;
import binding.ConstantBinding;
import token.ConstantToken;
import visitor.EtVisitor;

/**
 *
 * 文字定数 -> 文字定数トークン
 * @author hayas
 *
 */
public class VariableConstant extends Constant {
	private ConstantToken constantToken;


	@Override
	public boolean accept(final EtVisitor visitor) {
		visitor.visit(this);
		return visitor.leave(this);
	}

	@Override
	public VariableConstant setParent(final EtNode parent) {
		return (VariableConstant) super.setParent(parent);
	}

	public VariableConstant setChildren(final ConstantToken constantToken) {
		setConstantToken(constantToken);

		return this;
	}

	@Override
	public String toString() {
		return getConstantToken().toString();
	}

	@Override
	public VariableConstant copySubEt(final EtNode parent) {
		final VariableConstant variableConstant = new VariableConstant();

		final ConstantToken constantToken = getConstantToken().clone();

		return variableConstant.setParent(parent).setChildren(constantToken);
	}

	public ConstantToken getConstantToken() {
		return constantToken;
	}

	public VariableConstant setConstantToken(final ConstantToken constantToken) {
		if(constantToken.isConstant()) {
			this.constantToken = constantToken;
		} else {
			throw new NodeTypeException("not constant token: " + constantToken.getName());
		}
		return this;
	}

	public ConstantBinding resolveBinding() {
		return (ConstantBinding)Bindings.getInstance().getBinding(getConstantToken());
	}


}

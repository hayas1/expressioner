package tree;

import token.Controller;
import visitor.ConsDefVisitor;
import visitor.DefinitionVisitor;

/**
 *
 * 定数定義 -> "defcons" ["new"] 定数列 [":" 条件(左辺定数全てを含み、変数を含まない)]
 * @author hayas
 *
 */

public class ConstantDefinition extends DefinitionNode {
	private Controller def;
	private Controller newToken;
	private final static int CONSTANTS = 0;
	private final static int CONDITION = 1;


	@Override
	public boolean accept(final DefinitionVisitor visitor) {
		if(visitor.visit(this)) {
			getConstants().accept(visitor);
			if(hasCondition()) {
				getCondition().accept(visitor);
			}
		}

		return visitor.leave(this);
	}

	@Override
	public boolean isValidStructure() {
		return new ConsDefVisitor(this).isValidConstantDefinition();
	}

	@Override
	public ConstantDefinition setParent(final EtNode parent) {
		return (ConstantDefinition) super.setParent(parent);
	}

	public ConstantDefinition setChildren(final Controller def,final Controller newToken, final Constants constants, final Condition condition) {
		setDef(def);
		setNewToken(newToken);
		setConstants(constants);
		setCondition(condition);

		return this;
	}

	@Override
	public String toString() {
		final String newToken = hasNewToken()? getNewToken().toString(): "";
		final String condition = hasCondition()? Controller.COLON + " " + getCondition().toString(): "";

		return getDef().toString() + newToken + getConstants().toString() + condition;
	}

	@Override
	public ConstantDefinition copySubEt(final EtNode parent) {
		final ConstantDefinition definition = new ConstantDefinition();

		final Controller def = getDef().clone();
		final Controller newToken = hasNewToken()? getNewToken().clone(): null;
		final Constants constants = getConstants().copySubEt(definition);
		final Condition condition = hasCondition()? getCondition().copySubEt(definition): null;

		return definition.setParent(parent).setChildren(def, newToken, constants, condition);
	}

	public Controller getDef() {
		return def;
	}

	public ConstantDefinition setDef(final Controller def) {
		if(def != null) {
			this.def = def;
		} else {
			throw new NodeTypeException("don't allow null deffinition symbol");
		}
		return this;
	}

	public boolean hasNewToken() {
		return getNewToken() != null;
	}

	public Controller getNewToken() {
		return newToken;
	}

	public ConstantDefinition setNewToken(final Controller newToken) {
		this.newToken = newToken;
		return this;
	}

	public Constants getConstants() {
		return (Constants)super.getChild(CONSTANTS);
	}

	public ConstantDefinition setConstants(final Constants constants) {
		if(constants != null) {
			super.setChild(CONSTANTS, constants);
		} else {
			throw new NodeTypeException("don't allow null constants");
		}
		return this;
	}

	public boolean hasCondition() {
		return getCondition() != null;
	}

	public Condition getCondition() {
		return (Condition)super.getChild(CONDITION);
	}

	public ConstantDefinition setCondition(final Condition condition) {
		super.setChild(CONDITION, condition);
		return this;
	}
}

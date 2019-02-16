package parser.tree;

import lexer.tokens.Controller;
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
	private Constants constants;
	private Condition condition;


	@Override
	public boolean accept(final DefinitionVisitor visitor) {
		if(visitor.visit(this)) {
			getconstants().accept(visitor);
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

		return (ConstantDefinition) super.setChildrenNodes(constants, condition);
	}

	@Override
	public String toString() {
		final String newToken = hasNewToken()? getNewToken().toString(): "";
		final String condition = hasCondition()? Controller.COLON + " " + getCondition().toString(): "";

		return getDef().toString() + newToken + getconstants().toString() + condition;
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

	public Constants getconstants() {
		return constants;
	}

	public ConstantDefinition setConstants(final Constants constants) {
		if(constants != null) {
			this.constants = constants;
		} else {
			throw new NodeTypeException("don't allow null constants");
		}
		return this;
	}

	public boolean hasCondition() {
		return getCondition() != null;
	}

	public Condition getCondition() {
		return condition;
	}

	public ConstantDefinition setCondition(final Condition condition) {
		this.condition = condition;
		return this;
	}
}

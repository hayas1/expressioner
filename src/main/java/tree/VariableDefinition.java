package tree;

import token.Controller;
import visitor.DefinitionVisitor;
import visitor.VarDefVisitor;

/**
 *
 * 変数定義 -> "defvar" ["new"] 変数列 [":"　条件(左辺変数全て含む)]
 * @author hayas
 *
 */
public class VariableDefinition extends DefinitionNode {

	private Controller def;
	private Controller newToken;
	private static final int VARIABLES = 0;
	private static final int CONDITION = 1;


	@Override
	public boolean accept(final DefinitionVisitor visitor) {
		if(visitor.visit(this)) {
			getVariables().accept(visitor);
			if(hasCondition()) {
				getCondition().accept(visitor);
			}
		}

		return visitor.leave(this);
	}

	@Override
	public boolean isValidStructure() {
		return new VarDefVisitor(this).isValidVariableDefinition();
	}

	@Override
	public VariableDefinition setParent(final EtNode parent) {
		return (VariableDefinition) super.setParent(parent);
	}

	public VariableDefinition setChildren(final Controller def,final Controller newToken, final Variables variables, final Condition condition) {
		setDef(def);
		setNewToken(newToken);
		setVariables(variables);
		setCondition(condition);

		return (VariableDefinition) super.setChildrenNodes(variables, condition);
	}

	@Override
	public String toString() {
		final String newToken = hasNewToken()? getNewToken().toString(): "";
		final String condition = hasCondition()? Controller.COLON + " " + getCondition().toString(): "";

		return getDef().toString() + newToken + getVariables().toString() + condition;
	}

	public Controller getDef() {
		return def;
	}

	public VariableDefinition setDef(final Controller def) {
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

	public VariableDefinition setNewToken(final Controller newToken) {
		this.newToken = newToken;
		return this;
	}

	public Variables getVariables() {
		return (Variables)super.getChild(VARIABLES);
	}

	public VariableDefinition setVariables(final Variables variables) {
		if(variables != null) {
			super.setChild(VARIABLES, variables);
		} else {
			throw new NodeTypeException("don't allow null variables");
		}
		return this;
	}

	public boolean hasCondition() {
		return getCondition() != null;
	}

	public Condition getCondition() {
		return (Condition)super.getChild(CONDITION);
	}

	public VariableDefinition setCondition(final Condition condition) {
		super.setChild(CONDITION, condition);
		return this;
	}


}

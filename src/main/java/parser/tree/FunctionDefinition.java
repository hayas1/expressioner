package parser.tree;

import lexer.tokens.Controller;
import lexer.tokens.FunctionToken;
import visitor.DefinitionVisitor;
import visitor.FuncDefVisitor;

/**
 *
 * 関数定義 -> "deffunc" ["new"] 関数名 変数列 [":" 条件(変数は左辺定義済み変数のみを含み、それら全てを含む)]
 * @author hayas
 *
 */

public class FunctionDefinition extends DefinitionNode {

	private Controller def;
	private Controller newToken;
	private FunctionToken function;
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
		return new FuncDefVisitor(this).isValidFunctionDefinition();
	}

	@Override
	public FunctionDefinition setParent(final EtNode parent) {
		return (FunctionDefinition) super.setParent(parent);
	}

	public FunctionDefinition setChildren(final Controller def,final Controller newToken, final FunctionToken function, final Variables variables, final Condition condition) {
		setDef(def);
		setNewToken(newToken);
		setFunctionToken(function);
		setVariables(variables);
		setCondition(condition);

		return this;
	}

	@Override
	public String toString() {
		final String newToken = hasNewToken()? getNewToken().toString(): "";
		final String condition = hasCondition()? Controller.COLON + " " + getCondition().toString(): "";

		return getDef().toString() + newToken + getFunctionToken().toString() + getVariables().toString() + condition;
	}

	public Controller getDef() {
		return def;
	}

	public FunctionDefinition setDef(final Controller def) {
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

	public FunctionDefinition setNewToken(final Controller newToken) {
		this.newToken = newToken;
		return this;
	}

	public FunctionToken getFunctionToken() {
		return function;
	}

	public FunctionDefinition setFunctionToken(final FunctionToken function) {
		if(function != null) {
			this.function = function;
		} else {
			throw new NodeTypeException("don't allow null function token");
		}
		return this;
	}

	public Variables getVariables() {
		return (Variables)super.getChild(VARIABLES);
	}

	public FunctionDefinition setVariables(final Variables variables) {
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

	public FunctionDefinition setCondition(final Condition condition) {
		super.setChild(CONDITION, condition);
		return this;
	}


}

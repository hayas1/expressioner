package visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lexer.tokens.VariableToken;
import tree.FunctionDefinition;
import tree.Variable;
import tree.Variables;

/**
 *
 * 関数定義 -> "deffunc" ["new"] 関数名 変数列 [":" 条件(変数は左辺定義済み変数のみを含み、それら全てを含む)]
 * @author hayas
 *
 */
public class FuncDefVisitor extends DefinitionVisitor {
	private final List<VariableToken> defined = new ArrayList<>();
	private final Set<VariableToken> appeared = new HashSet<>();

	private boolean isValid = true;

	public FuncDefVisitor(final FunctionDefinition definition) {
		definition.accept(this);
	}

	@Override
	public boolean visit(final Variables node) {
		defined.addAll(node.getVariables());
		setValid(!node.hasParens());
		return super.visit(node);
	}

	@Override
	public boolean visit(final Variable node) {
		appeared.add(node.getVariableToken());
		setValid(!defined.contains(node.getVariableToken()));
		return super.visit(node);
	}

	@Override
	public boolean leave(final FunctionDefinition node) {
		defined.removeAll(appeared);
		setValid(defined.isEmpty());
		return super.leave(node);
	}


	public void setValid(final boolean isValid) {
		if(this.isValid == true) {
			this.isValid =isValid;
		}
	}

	public boolean isValidFunctionDefinition() {
		return isValid;
	}
}

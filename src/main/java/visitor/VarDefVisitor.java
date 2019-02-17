package visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import token.VariableToken;
import tree.Variable;
import tree.VariableDefinition;
import tree.Variables;

/**
 *
 * 変数定義 -> "defvar" ["new"] 変数列 [":"　条件(左辺変数全て含む)]
 * @author hayas
 *
 */
public final class VarDefVisitor extends DefinitionVisitor {
	private final List<VariableToken> defined = new ArrayList<>();
	private final Set<VariableToken> appeared = new HashSet<>();

	private boolean isValid = true;

	public VarDefVisitor(final VariableDefinition definition) {
		definition.accept(this);
	}

	@Override
	public boolean visit(final Variables node) {
		defined.addAll(node.getVariables());
		setValid(node.hasParens());
		return super.visit(node);
	}

	@Override
	public boolean visit(final Variable node) {
		appeared.add(node.getVariableToken());
		return super.visit(node);
	}

	@Override
	public boolean leave(VariableDefinition node) {
		defined.removeAll(appeared);
		setValid(defined.isEmpty());
		return super.leave(node);
	}


	public void setValid(final boolean isValid) {
		if(this.isValid == true) {
			this.isValid = isValid;
		}
	}

	public boolean isValidVariableDefinition() {
		return isValid;
	}
}

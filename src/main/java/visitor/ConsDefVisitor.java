package visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lexer.tokens.ConstantToken;
import parser.tree.ConstantDefinition;
import parser.tree.Constants;
import parser.tree.Variable;
import parser.tree.VariableConstant;

/**
 *
 * 定数定義 -> "defcons" ["new"] 定数列 [":" 条件(左辺定数全てを含み、変数を含まない)]
 * @author hayas
 *
 */

public final class ConsDefVisitor extends DefinitionVisitor {
	private final List<ConstantToken> defined = new ArrayList<>();
	private final Set<ConstantToken> appeared = new HashSet<>();

	private boolean isValid = true;

	public ConsDefVisitor(final ConstantDefinition definition) {
		definition.accept(this);
	}


	@Override
	public boolean visit(final Constants node) {
		defined.addAll(node.getConstants());
		return super.visit(node);
	}

	@Override
	public boolean visit(final VariableConstant node) {
		appeared.add(node.getConstantToken());
		return super.visit(node);
	}

	@Override
	public boolean visit(final Variable node) {
		setValid(false);
		return super.visit(node);
	}

	@Override
	public boolean leave(ConstantDefinition node) {
		defined.removeAll(appeared);
		setValid(defined.isEmpty());
		return super.leave(node);
	}


	public void setValid(final boolean isValid) {
		if(this.isValid == true) {
			this.isValid = isValid;
		}
	}

	public boolean isValidConstantDefinition() {
		return isValid;
	}


}

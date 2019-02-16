package visitor;

import tree.ConstantDefinition;
import tree.Constants;
import tree.FunctionDefinition;
import tree.VariableDefinition;
import tree.Variables;

public class DefinitionVisitor extends EtVisitor{

	public boolean visit(final VariableDefinition node) {
		return true;
	}

	public boolean leave(final VariableDefinition node) {
		return true;
	}

	public boolean visit(final Variables node) {
		return true;
	}

	public boolean leave(final Variables node) {
		return true;
	}

	public boolean visit(final ConstantDefinition node) {
		return true;
	}

	public boolean leave(final ConstantDefinition node) {
		return true;
	}

	public boolean visit(final Constants node) {
		return true;
	}

	public boolean leave(final Constants node) {
		return true;
	}

	public boolean visit(final FunctionDefinition node) {
		return true;
	}

	public boolean leave(final FunctionDefinition node) {
		return true;
	}

}

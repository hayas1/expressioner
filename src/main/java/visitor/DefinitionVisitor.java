package visitor;

import parser.tree.ConstantDefinition;
import parser.tree.Constants;
import parser.tree.FunctionDefinition;
import parser.tree.VariableDefinition;
import parser.tree.Variables;

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

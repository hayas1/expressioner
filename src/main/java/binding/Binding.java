package binding;

import parser.tree.DefinitionNode;

public abstract class Binding {
	private final DefinitionNode definition;

	public Binding(final DefinitionNode definition) {
		this.definition = definition;
	}


	public DefinitionNode getDefinition() {
		return definition;
	}

}

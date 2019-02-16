package parser.tree;

import visitor.DefinitionVisitor;
import visitor.EtVisitor;
import visitor.VisitorException;

public abstract class DefinitionNode extends EtNode {

	public abstract boolean accept(final DefinitionVisitor visitor);

	@Override
	public DefinitionNode setParent(final EtNode parent) {
		return (DefinitionNode) super.setParent(parent);
	}

	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor instanceof DefinitionVisitor) {
			return this.accept((DefinitionVisitor)visitor);
		} else {
			throw new VisitorException("this class must be visitored by DefinitionVisitor class");
		}
	}

	public boolean isValidStructure() {
		return true;
	}
}

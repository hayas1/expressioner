package visitor;

import java.util.Stack;

import tree.Expression;
import tree.Term;

/**
 * 式木を探索し、約分可能な部分木を発見する。
 * @author hayas
 *
 */
public class ReductionVisitor extends EtVisitor {
	private final Stack<Term> stack = new Stack<>();
	private boolean isExpression;
	
	
	@Override
	public boolean visit(final Expression node) {
		isExpression = true;
		return super.visit(node);
	}
	
	@Override
	public boolean visit(final Term node) {
		if(isExpression) {
			
		}
		return super.visit(node);
	}

	@Override
	public boolean leave(final Term node) {
		
		return super.leave(node);
	}
	
	@Override
	public boolean leave(final Expression node) {
		// TODO 自動生成されたメソッド・スタブ
		return super.leave(node);
	}
}

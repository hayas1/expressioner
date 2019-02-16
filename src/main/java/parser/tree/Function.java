package parser.tree;

import binding.Bindings;
import binding.FunctionBinding;
import lexer.tokens.FunctionToken;
import lexer.tokens.Operator;
import visitor.EtVisitor;

/**
 *
 * 関数 -> 関数名 ['] 引数
 * @author hayas
 *
 */

public class Function extends Factor {
	private FunctionToken functionToken;
	private Operator prime;
	private static final int ARGUMENT = 0;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getArgument().accept(visitor);
		}
		return visitor.leave(this);
	}

	@Override
	public Function setParent(EtNode parent) {
		return (Function)super.setParent(parent);
	}

	public Function setChildren(final FunctionToken functionToken, final Operator prime, final Argument argument) {
		setFunctionToken(functionToken);
		setPrime(prime);
		setArgument(argument);

		return this;
	}

	@Override
	public String toString() {
		if(hasPrime()) {
			return getFunctionToken().toString() + getPrime().toString() + getArgument().toString();
		} else {
			return getFunctionToken().toString() + getArgument().toString();
		}
	}


	public FunctionToken getFunctionToken() {
		return functionToken;
	}

	public Function setFunctionToken(final FunctionToken functionToken) {
		if(functionToken != null) {
			this.functionToken = functionToken;
		} else {
			throw new NodeTypeException("don't allow null name function");
		}
		return this;
	}

	public boolean hasPrime() {
		return getPrime() != null;
	}

	public Operator getPrime() {
		return prime;
	}

	public Function setPrime(final Operator prime) {
		if(prime==null || prime.isDifferential()) {
			this.prime = prime;
		} else {
			throw new NodeTypeException("not prime operator: " + prime.getName());
		}
		return this;
	}

	public Argument getArgument() {
		return (Argument)super.getChild(ARGUMENT);
	}

	public Function setArgument(final Argument argument) {
		if(argument != null) {
			super.setChild(ARGUMENT, argument);
		} else {
			throw new NodeTypeException("don't allow null argument function");
		}
		return this;
	}

	public FunctionBinding resolveBinding() {
		return (FunctionBinding)Bindings.getInstance().getBinding(getFunctionToken());
	}



}

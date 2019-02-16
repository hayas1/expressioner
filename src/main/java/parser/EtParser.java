package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import binding.Bindings;
import binding.ConstantBinding;
import binding.FunctionBinding;
import binding.VariableBinding;
import lexer.tokens.ConstantToken;
import lexer.tokens.FunctionToken;
import lexer.tokens.Operator;
import lexer.tokens.Paren;
import lexer.tokens.Separator;
import lexer.tokens.Token;
import lexer.tokens.VariableToken;
import parser.tree.Argument;
import parser.tree.Condition;
import parser.tree.Constant;
import parser.tree.EtNode;
import parser.tree.Expression;
import parser.tree.Expressions;
import parser.tree.Factor;
import parser.tree.FactorExpression;
import parser.tree.Function;
import parser.tree.MainExpression;
import parser.tree.NumberConstant;
import parser.tree.PowerFactor;
import parser.tree.Term;
import parser.tree.Variable;
import parser.tree.VariableConstant;

/**
 * ・条件 -> 主式 関係演算子 主式 <br>
 * ・主式 -> [符号] 式 <br>
 * ・式 -> 項 [加法演算子 式] <br>
 * ・項 -> 累乗因子 [[乗法演算子] 項] <br>
 * ・累乗因子 -> 因子 [累乗演算子 累乗因子] <br>
 * ・因子 -> 変数|定数|因子式|関数 <br>
 * ・定数 -> 文字定数|数字定数 <br>
 * ・数字定数 -> 数字[.数字] <br>
 * ・因子式 -> 左括弧 主式 右括弧 <br>
 * ・関数 -> 関数名 ['] 引数 <br>
 * ・引数 -> 累乗因子|式列 <br>
 * ・式列 -> 左括弧 主式{"," 主式} 右括弧 <br>
 *
 * 項の乗法演算子は選択可能であるが、スペースを開けなくてよいのはそのどちらの因子も1文字の変数である場合のみ。
 * 引数のない関数は許さない。
 * 割り算後の掛け算は分母で扱う(100/3aは(100)/(3*a)と解釈、100/3a/2bは(100)/((3*a)/(2*b))と解釈
 *
 *
 * @author hayas
 */
public class EtParser {
	private final List<Token> tokens;
	private final ListIterator<Token> iterator;

	private Token currentToken = null;

	public EtParser(final List<Token> tokens) {
		this.tokens = new ArrayList<>(tokens);
		this.iterator = tokens.listIterator();
	}

	public List<Token> getTokens(){
		return tokens;
	}

	public EtNode createConditionEt() {
		getCurrentReadNext();
		return condition(null);
	}

	public EtNode createExpressionEt() {
		getCurrentReadNext();
		return mainExpression(null);
	}

	protected Token getCurrentToken() {
		return currentToken;
	}

	protected Token getCurrentReadNext() {
		final Token currentToken = getCurrentToken();
		if(iterator.hasNext()) {
			this.currentToken  = iterator.next();
		} else {
			this.currentToken = Token.createDummy();
		}
		return currentToken;
	}

	//condition() -> mainExpression() relationalOperator() mainExpression()
	protected Condition condition(final EtNode parent) {
		final Condition condition = new Condition();

		final MainExpression leftHandSide = mainExpression(condition);
		final Operator relationalOperator = relationalOperator();
		final MainExpression rightHandSide = mainExpression(condition);

		return condition.setParent(parent).setChildren(leftHandSide, relationalOperator, rightHandSide);
	}

	protected Operator relationalOperator() {
		if(getCurrentToken().isRelationalOperator()) {
			return (Operator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected relational operator, but unexpected token: ", getCurrentToken());
		}
	}


	//mainExpression() -> [sign()] expression()
	protected MainExpression mainExpression(final EtNode parent) {
		final MainExpression mainExpression = new MainExpression();

		Operator sign = null;
		if(getCurrentToken().isSignOperator()) {
			sign = sign();
		}

		final Expression expression = expression(mainExpression);


		return mainExpression.setParent(parent).setChildren(sign,expression);
	}

	protected Operator sign() {
		if(getCurrentToken().isSignOperator()) {
			return (Operator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected sign token, but unexpected token: ", getCurrentToken());
		}
	}

	//expression() -> term() [additiveOperator() expression()]
	protected Expression expression(final EtNode parent) {
		final Expression expression = new Expression();

		final Term term = term(expression);

		Operator additiveOperator = null;
		Expression postExpression = null;
		if(getCurrentToken().isAdditiveOperator()) {
			additiveOperator = additiveOperator();
			postExpression = expression(expression);
		}

		return expression.setParent(parent).setChildren(term, additiveOperator, postExpression);
	}

	protected Operator additiveOperator() {
		if(getCurrentToken().isAdditiveOperator()) {
			return (Operator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected additive operator, but unexpected token: ", getCurrentToken());
		}
	}

	//term() -> powerfactor() [[multiplicativeOperator()] term()]
	protected Term term(final EtNode parent) {
		final Term term = new Term();

		final PowerFactor factor = powerFactor(term);

		Operator multiplicativeOperator = null;
		Term postTerm = null;
		if(getCurrentToken().isFactorBeginnable()) {		//term start with factor
			postTerm = term(term);
		} else if(getCurrentToken().isMultiplicativeOperator()) {
			multiplicativeOperator = multiplicativeOperator();
			postTerm = term(term);
		}


		return term.setParent(parent).setChildren(factor, multiplicativeOperator, postTerm);
	}

	protected Operator multiplicativeOperator() {
		if(getCurrentToken().isMultiplicativeOperator()) {
			return (Operator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected multiplicative operator, but unexpected token: ", getCurrentToken());
		}
	}

	//powerFactor() -> factor() [ powerOperator() powerFactor()]
	protected PowerFactor powerFactor(final EtNode parent) {
		final PowerFactor powerFactor = new PowerFactor();

		final Factor factor = factor(powerFactor);

		Operator powerOperator = null;
		PowerFactor power = null;
		if(getCurrentToken().isPower()) {
			powerOperator = powerOperator();
			power = powerFactor(powerFactor);
		}

		return powerFactor.setParent(parent).setChildren(factor, powerOperator, power);
	}

	protected Operator powerOperator() {
		if(getCurrentToken().isPower()) {
			return (Operator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected power operator, but unexpected token: ", getCurrentToken());
		}
	}

	//factor() -> variable() | constant() | factorExpression() | function()
	protected Factor factor(final EtNode parent) {
		if(getCurrentToken().isVariable()) {
			return variable(parent);
		} else if(getCurrentToken().isConstant()) {
			return constant(parent);
		} else if(getCurrentToken().isLeftParen()) {
			return factorExpression(parent);
		} else if(getCurrentToken().isFunction()) {
			return function(parent);
		} else {
			throw new SyntaxException("expected factor token, but unexpected token: ", getCurrentToken());
		}

	}

	//variable() -> variableToken
	protected Variable variable(final EtNode parent) {
		final Variable variable = new Variable();

		final VariableToken variableToken = variableToken();
		Bindings.getInstance().put(getCurrentToken(), new VariableBinding(null));

		return variable.setParent(parent).setChildren(variableToken);
	}

	protected VariableToken variableToken() {
		if(getCurrentToken().isVariable()) {
			return (VariableToken)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected variable token, but unexpected token: ", getCurrentToken());
		}
	}

	//constant() -> numberConstant() | variableConstant()
	protected Constant constant(final EtNode parent) {
		if(getCurrentToken().isNumber()) {
			return numberConstant(parent);
		} else if(getCurrentToken().isConstant()) {
			return variableConstant(parent);
		} else {
			throw new SyntaxException("expected constant token, but unexpected token: ", getCurrentToken());
		}

	}

	protected ConstantToken constantToken() {
		if(getCurrentToken().isConstant()) {
			return (ConstantToken)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected variable constant token, but unexpected token: ", getCurrentToken());
		}
	}

	//variableConstant() -> constantToken
	protected VariableConstant variableConstant(final EtNode parent) {
		final VariableConstant constant = new VariableConstant();

		final ConstantToken variableConstant = constantToken();
		Bindings.getInstance().put(getCurrentToken(), new ConstantBinding(null));

		return constant.setParent(parent).setChildren(variableConstant);
	}

	//constant() -> constantToken [. constantToken]
	protected NumberConstant numberConstant(final EtNode parent) {
		final ConstantToken integer = constantToken();

		Separator point = null;
		ConstantToken decimal = null;
		if(getCurrentToken().isPoint()) {
			point = point();
			decimal = constantToken();
		}

		return new NumberConstant().setParent(parent).setChildren(integer, point, decimal);
	}

	protected ConstantToken numberConstant() {
		if(getCurrentToken().isNumber()) {
			return (ConstantToken)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected number constant token, but unexpected token: ", getCurrentToken());
		}
	}

	protected Separator point() {
		if(getCurrentToken().isPoint()) {
			return (Separator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected point token, but unexpected token: ", getCurrentToken());
		}
	}

	//factorExpression -> leftparen() expression() rightParen()
	protected FactorExpression factorExpression(final EtNode parent) {
		final FactorExpression factorExpression = new FactorExpression();

		final Paren leftParen = leftParen();
		final MainExpression expression = mainExpression(factorExpression);
		final Paren rightParen = rightParen();

		return factorExpression.setParent(parent).setChildren(leftParen, expression, rightParen);
	}

	protected Paren leftParen() {
		if(getCurrentToken().isLeftParen()) {
			return (Paren)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected left paren token, but unexpected token: ", getCurrentToken());
		}
	}

	protected Paren rightParen() {
		if(getCurrentToken().isRightParen()) {
			return (Paren)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected right paren token, but unexpected token: ", getCurrentToken());
		}
	}

	//function() -> functionToken [prime()] argument()
	protected Function function(final EtNode parent) {
		final Function function = new Function();

		final FunctionToken functionToken = functionToken();
		Bindings.getInstance().put(getCurrentToken(), new FunctionBinding(null));

		Operator prime = null;
		if(getCurrentToken().isDifferential()) {
			prime = prime();
		}

		final Argument argument = argument(function);

		return function.setParent(parent).setChildren(functionToken, prime, argument);
	}

	protected FunctionToken functionToken() {
		if(getCurrentToken().isFunction()) {
			return (FunctionToken)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected function token, but unexpected token: ", getCurrentToken());
		}
	}

	protected Operator prime() {
		if(getCurrentToken().isDifferential()) {
			return (Operator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected diffrential token, but unexpected token: ", getCurrentToken());
		}
	}

	//argument() -> [factor() | expressions()]
	protected Argument argument(final EtNode parent) {		//TODO i think very bad coding
		try {
			return powerFactor(parent);
		} catch (final SyntaxException e) {
			if(e.getToken().isComma()) {
				return expressions(parent);
			} else {
				throw new SyntaxException("expected comma token, but unexpected token: ", e.getToken());
			}
		}

	}

	//expressions() -> leftParen() expression() {, expression()}  rightParen()
	protected Expressions expressions(final EtNode parent) {
		final Expressions expressions = new Expressions();
		final List<MainExpression> expressionList = new ArrayList<>();

		final Paren leftParen = leftParen();
		expressionList.add(mainExpression(expressions));

		while(getCurrentToken().isComma()) {
			comma();
			expressionList.add(mainExpression(expressions));
		}

		final Paren rightParen = rightParen();

		return expressions.setParent(parent).setChildren(leftParen, expressionList, rightParen);

	}

	protected Separator comma() {
		if(getCurrentToken().isComma()) {
			return (Separator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected comma token, but unexpected token: ", getCurrentToken());
		}
	}

}

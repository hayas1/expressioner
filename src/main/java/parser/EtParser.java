package parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import binding.Bindings;
import binding.ConstantBinding;
import binding.FunctionBinding;
import binding.VariableBinding;
import token.ConstantToken;
import token.DigitToken;
import token.FunctionToken;
import token.Operator;
import token.Paren;
import token.Separator;
import token.Token;
import token.VariableToken;
import tree.Argument;
import tree.Condition;
import tree.Constant;
import tree.DigitConstant;
import tree.EtNode;
import tree.Expression;
import tree.Expressions;
import tree.Factor;
import tree.FactorExpression;
import tree.Function;
import tree.MainExpression;
import tree.MainTerm;
import tree.PowerFactor;
import tree.Term;
import tree.Variable;
import tree.VariableConstant;

/**
 * ・条件 -> 主式 関係演算子 主式 <br>
 * ・主式 -> [符号] 式 <br>
 * ・式 -> 主項 [加法演算子 式] <br>
 * ・主項 -> 項 <br>
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
 * 割り算後の掛け算は分母として扱わない。つまりab/cd=a*b/c*dであり、ab/(cd)=a*b/c/dである。
 *
 *
 * @author hayas
 */
public class EtParser {
	private final List<Token> tokens;
	private final Iterator<Token> iterator;

	private Token currentToken = null;

	public EtParser(final List<Token> tokens) {
		this.tokens = new ArrayList<>(tokens);
		this.iterator = tokens.iterator();

		getCurrentReadNext();
	}

	public List<Token> getTokens(){
		return tokens;
	}

	public Condition createConditionEt() {
		final Condition condition = condition(null);

		if(!hasNextToken()) {
			return condition;
		} else {
			throw new SyntaxException("not condition, unexpected token: ", iterator.next());
		}
	}

	public MainExpression createMainExpressionEt() {
		final MainExpression mainExpression = mainExpression(null);

		if(!hasNextToken()) {
			return mainExpression;
		} else {
			throw new SyntaxException("not main expression, unexpected token: ", iterator.next());
		}

	}

	public Expression createExpressionEt() {
		final Expression expression = expression(null);

		if(!hasNextToken()) {
			return expression;
		} else {
			throw new SyntaxException("not expression, unexpected token: ", iterator.next());
		}
	}

	public MainTerm createMainTermEt() {
		final MainTerm term = mainTerm(null);

		if(!hasNextToken()) {
			return term;
		} else {
			throw new SyntaxException("not term, unexpected token: ", iterator.next());
		}
	}

	public Term createTermEt() {
		final Term term = term(null);

		if(!hasNextToken()) {
			return term;
		} else {
			throw new SyntaxException("not term, unexpected token: ", iterator.next());
		}
	}

	public PowerFactor createPowerFactorEt() {
		final PowerFactor powerFactor = powerFactor(null);

		if(!hasNextToken()) {
			return powerFactor;
		} else {
			throw new SyntaxException("not power factor, unexpected token: ", iterator.next());
		}
	}

	public Factor createFactorEt() {
		final Factor factor = factor(null);

		if(!hasNextToken()) {
			return factor;
		} else {
			throw new SyntaxException("not factor, unexpected token: ", iterator.next());
		}
	}

	public Constant createConstantEt() {
		final Constant constant = constant(null);

		if(!hasNextToken()) {
			return constant;
		} else {
			throw new SyntaxException("not constant, unexpected token: ", iterator.next());
		}
	}

	public DigitConstant createDigitConstantEt() {
		final DigitConstant numberConstant = digitConstant(null);

		if(!hasNextToken()) {
			return numberConstant;
		} else {
			throw new SyntaxException("not number constant, unexpected token: ", iterator.next());
		}
	}

	public FactorExpression createFactorExpressionEt() {
		final FactorExpression factorExpression = factorExpression(null);

		if(!hasNextToken()) {
			return factorExpression;
		} else {
			throw new SyntaxException("not factor expression, unexpected token: ", iterator.next());
		}
	}

	public Function createFunctionEt() {
		final Function function = function(null);

		if(!hasNextToken()) {
			return function;
		} else {
			throw new SyntaxException("not function, unexpected token: ", iterator.next());
		}
	}

	public Argument createArgumentEt() {
		final Argument argument = argument(null);

		if(!hasNextToken()) {
			return argument;
		} else {
			throw new SyntaxException("not argument, unexpected token: ", iterator.next());
		}
	}

	public Expressions createExpressionsEt() {
		final Expressions expressions = expressions(null);

		if(!hasNextToken()) {
			return expressions;
		} else {
			throw new SyntaxException("not expressions, unexpected token: ", iterator.next());
		}
	}


	protected Token getCurrentToken() {
		return currentToken;
	}

	protected boolean hasNextToken() {
		return iterator.hasNext();
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

	//expression() -> mainTerm() [additiveOperator() expression()]
	protected Expression expression(final EtNode parent) {
		final Expression expression = new Expression();

		final MainTerm mainTerm = mainTerm(expression);

		Operator additiveOperator = null;
		Expression postExpression = null;
		if(getCurrentToken().isAdditiveOperator()) {
			additiveOperator = additiveOperator();
			postExpression = expression(expression);
		}

		return expression.setParent(parent).setChildren(mainTerm, additiveOperator, postExpression);
	}

	protected Operator additiveOperator() {
		if(getCurrentToken().isAdditiveOperator()) {
			return (Operator)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected additive operator, but unexpected token: ", getCurrentToken());
		}
	}

	//mainTerm() -> term()
	protected MainTerm mainTerm(final EtNode parent) {
		final MainTerm mainTerm = new MainTerm();

		final Term term = term(mainTerm);

		return mainTerm.setParent(parent).setChildren(term);
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
		} else if(getCurrentToken().isConstant() || getCurrentToken().isDigit()) {
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

	//constant() -> digitConstant() | variableConstant()
	protected Constant constant(final EtNode parent) {
		if(getCurrentToken().isDigit()) {
			return digitConstant(parent);
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

	//constant() -> digitToken [. digitToken]
	protected DigitConstant digitConstant(final EtNode parent) {
		final DigitToken integer = digitToken();

		Separator point = null;
		DigitToken decimal = null;
		if(getCurrentToken().isPoint()) {
			point = point();
			decimal = digitToken();
		}

		return new DigitConstant().setParent(parent).setChildren(integer, point, decimal);
	}

	protected DigitToken digitToken() {
		if(getCurrentToken().isDigit()) {
			return (DigitToken)getCurrentReadNext();
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

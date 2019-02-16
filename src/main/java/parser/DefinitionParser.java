package parser;

import java.util.ArrayList;
import java.util.List;

import lexer.tokens.ConstantToken;
import lexer.tokens.Controller;
import lexer.tokens.FunctionToken;
import lexer.tokens.Paren;
import lexer.tokens.Token;
import lexer.tokens.VariableToken;
import tree.Condition;
import tree.ConstantDefinition;
import tree.Constants;
import tree.DefinitionNode;
import tree.EtNode;
import tree.FunctionDefinition;
import tree.VariableDefinition;
import tree.Variables;

/**
 * ・定義 -> 変数定義 | 定数定義 | 関数定義 <br>
 * ・変数定義 -> "defvar" ["new"] 変数列 [":"　条件(左辺変数全て含む)] <br>
 * ・定数定義 -> "defcons" ["new"] 定数列 [":" 条件(左辺定数全てを含み、変数を含まない)] <br>
 * ・関数定義 -> "deffunc" ["new"] 関数名 変数列 [":" 条件(変数は左辺定義済み変数のみを含み、それらすべてを含む)] <br>
 * @author hayas
 *
 */

public class DefinitionParser extends EtParser {

	public DefinitionParser(final List<Token> tokens) {
		super(tokens);
	}


	public EtNode createDefinitionEt() {
		getCurrentReadNext();
		final DefinitionNode definitionNode = definition(null);
		if(definitionNode.isValidStructure()) {
			return definitionNode;
		} else {
			throw new SyntaxException("illegal definition", null);		//TODO indicate illegal reason token(change visitor)
		}
	}


	//definition() -> variableDefinition() | constantDefinition() | functionDefinition()
	protected DefinitionNode definition(final EtNode parent) {
		final Controller def = def();

		if(def.isDefVar()) {
			return variableDefinition(parent, def);
		} else if(def.isDefCons()) {
			return constantDefinition(parent, def);
		} else if(def.isDefFunc()) {
			return functionDefinition(parent, def);
		} else {
			throw new SyntaxException("expected def token, but unexpected token: ", getCurrentToken());
		}
	}

	protected Controller def() {
		if(getCurrentToken().isDef()) {
			return (Controller)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected def token, but unexpected token: ", getCurrentToken());
		}
	}

	protected Controller newToken() {
		if(getCurrentToken().isNew()) {
			return (Controller)getCurrentReadNext();
		} else {
			return null;
		}
	}

	protected Controller colon() {
		if(getCurrentToken().isColon()) {
			return (Controller)getCurrentReadNext();
		} else {
			throw new SyntaxException("expected colon token, but unexpected token: ", getCurrentToken());
		}
	}

	//variableDefinition() -> "defvar" ["new"] variables() [":" condition()]
	protected VariableDefinition variableDefinition(final EtNode parent, final Controller defSymbol) {
		final VariableDefinition variableDefinition = new VariableDefinition();
		final Controller def = defSymbol;
		final Controller newToken = newToken();
		final Variables variables = variables(variableDefinition);

		Condition condition = null;
		if(getCurrentToken().isColon()) {
			colon();
			condition = super.condition(variableDefinition);
		}

		return variableDefinition.setParent(parent).setChildren(def, newToken, variables, condition);
	}

	//variables() -> [leftParen()] variable() {"," variable()} [rightParen()]
	protected Variables variables(final EtNode parent) {
		final Variables variables = new Variables();
		final List<VariableToken> variableList = new ArrayList<>();

		final Paren leftParen = null;
		variableList.add(super.variableToken());

		while(getCurrentToken().isComma()) {
			comma();
			variableList.add(super.variableToken());
		}

		final Paren rightParen = null;

		return variables.setParent(parent).setChildren(leftParen, variableList, rightParen);
	}

	//constantDefinition() -> "defcons" ["new"] constants() [":" condition()]
	protected ConstantDefinition constantDefinition(final EtNode parent, final Controller defSymbol) {
		final ConstantDefinition constantDefinition = new ConstantDefinition();
		final Controller def = defSymbol;
		final Controller newToken = newToken();
		final Constants constants = constants(constantDefinition);

		Condition condition = null;
		if(getCurrentToken().isColon()) {
			colon();
			condition = super.condition(constantDefinition);
		}

		return constantDefinition.setParent(parent).setChildren(def, newToken, constants, condition);
	}

	protected Constants constants(final EtNode parent) {
		final Constants constants = new Constants();
		final List<ConstantToken> constantList = new ArrayList<>();

		final Paren leftParen = null;
		constantList.add(super.constantToken());

		while(getCurrentToken().isComma()) {
			comma();
			constantList.add(super.constantToken());
		}

		final Paren rightParen = null;

		return constants.setParent(parent).setChildren(leftParen, constantList, rightParen);
	}

	//functionDefinition() -> "deffunc" ["new"] functionToken() variables() [":" condition()]
	protected FunctionDefinition functionDefinition(final EtNode parent, final Controller defSymbol) {
		final FunctionDefinition functionDefinition = new FunctionDefinition();
		final Controller def = defSymbol;
		final Controller newToken = newToken();
		final FunctionToken functionToken = super.functionToken();
		final Variables variables = variables(functionDefinition);

		Condition condition = null;
		if(getCurrentToken().isColon()) {
			colon();
			condition = super.condition(functionDefinition);
		}

		return functionDefinition.setParent(parent).setChildren(def, newToken,functionToken, variables, condition);
	}
}

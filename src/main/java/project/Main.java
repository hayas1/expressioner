package project;

import java.util.List;

import lexer.Lexer;
import parser.EtParser;
import token.DigitToken;
import token.Token;
import tree.EtNode;
import visitor.EtVisitor;

public class Main {


	public static void main(final String[] args) {
		System.out.println(DigitToken.divide((DigitToken)Token.create("200"), (DigitToken)Token.create("10"),3));

		String expressiontmp = "1*(2*3)>1+(2+3)";
		String expression1 = "100x^2 + 11x+10=0";
		String expression2 = "100x^2+11x+10>=0";
		String expression3 = "100xyz+x=0";
		String expression4 = "sinx+x=0";
		String expression5 = "x1y+x2=0";
		String expression6 = "100.12";
		String expression7 = "10xsin10=100";
		String expression8 = "100+x = 20";
		String expression9 = "100a/20(10/40b)=x";



		List<Token> tokens = new Lexer(expressiontmp).tokenize();
		EtNode expression = new EtParser(tokens).createConditionEt();
		System.out.println(expression);

		EtVisitor testVisitor = new EtVisitor() {
			public boolean visit(tree.FactorExpression node) {
				node.removeParen(false);
				return super.visit(node);
			}
		};

		expression.accept(testVisitor);

		System.out.println("----replace----");
		System.out.println(expression);

	}

}

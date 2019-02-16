package project;

import java.util.List;

import lexer.Lexer;
import lexer.tokens.Token;
import parser.EtParser;
import parser.tree.EtNode;
import visitor.EtVisitor;

public class Main {


	public static void main(final String[] args) {
		String expression1 = "100x^2 + 11x+10=0";
		String expression2 = "100x^2+11x+10>=0";
		String expression3 = "100xyz+x=0";
		String expression4 = "sinx+x=0";
		String expression5 = "x1y+x2=0";
		String expression6 = "100.12";
		String expression7 = "10xsin10=100";
		String expression8 = "100+x = 20";


		List<Token> tokens = new Lexer(expression8).tokenize();
		EtNode expression = new EtParser(tokens).createConditionEt();
		System.out.println(expression);

		EtVisitor testVisitor = new EtVisitor() {
			public boolean visit(parser.tree.Variable node) {
				node.replace(new EtParser(new Lexer("a+b+c").tokenize()).createExpressionEt());
				return super.visit(node);
			}
		};

		expression.accept(testVisitor);

		System.out.println("----replace----");
		System.out.println(expression);

	}

}

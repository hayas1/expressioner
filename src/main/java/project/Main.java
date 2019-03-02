package project;

import java.util.ArrayList;
import java.util.List;

import lexer.Lexer;
import parser.EtParser;
import token.Token;
import tree.Condition;
import tree.DigitConstant;
import tree.EtNode;
import tree.Term;
import visitor.EtVisitor;

public class Main {


	public static void main(final String[] args) {
		System.out.println(1+0.3*18);
		final DigitConstant a = new EtParser(new Lexer("1145.14").tokenize()).createDigitConstantEt();
		final DigitConstant b = new EtParser(new Lexer("1.54").tokenize()).createDigitConstantEt();
		//System.out.println(DigitConstant.divide(a, b, 2));
		System.out.println(DigitConstant.times(a, b));

		//		System.out.println(b.toFraction());
		//System.out.println(DigitToken.plus((DigitToken)Token.create("200"), (DigitToken)Token.create("110")));

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



		List<Token> tokens = new Lexer(expression1).tokenize();
		EtNode expression = new EtParser(tokens).createConditionEt();
		//System.out.println(expression);

		EtVisitor testVisitor = new EtVisitor() {
			List<Term> list = new ArrayList<>();
			@Override
			public boolean visit(/*Main*/Term node) {
				list.add(node);
				return super.visit(node);
			}

			@Override
			public boolean leave(Condition node) {
				list.forEach(nod -> nod.times(new EtParser(new Lexer("a").tokenize()).createPowerFactorEt()));
				return super.leave(node);
			}
		};

		expression.accept(testVisitor);

		System.out.println("----replace----");
		System.out.println(expression);

	}

}

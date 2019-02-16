//package lexer;
//
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//
//import lexer.tokens.Token;
//
//
//public class FactorBuilder {
//	private final TokenTable tokenTable = TokenTable.getInstance();
//	private final String factors;
//	private final LinkedList<String> built = new LinkedList<>();
//	private final Iterator<String> iterator;
//
//	public FactorBuilder(final String term) {
//		this.factors = term;
//		this.iterator = Arrays.asList(term.split("")).iterator();
//		if(Token.isValidName(term)) {
//			this.build();		//xsin10 -> x sin 10, x1y -> x1 y, xyz -> x y z
//		} else {
//			built.add(term);
//		}
//	}
//
//
//
//	public List<String> built(){
//		return built;
//	}
//
//
//
//	}
//
//	private void checkVariableOrConstant() {
//
//	}
//
//
//
//
//}

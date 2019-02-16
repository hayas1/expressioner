package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.Inputer;
import lexer.tokens.Token;

public class TokenTable {
	public static String OPERANDS_CSV_PATH = "data/definedOperand.csv";
	public static String OPERATORS_CSV_PATH = "data/definedOperator.csv";
	public static String TOKENS_CSV_PATH = "data/definedToken.csv";

	private static final TokenTable instance = new TokenTable();
	private final Map<String, String> operands = new HashMap<>();
	private final Map<String, String> operators = new HashMap<>();
	private final Map<String, String> tokens = new HashMap<>();
	private final Map<String, String> userDefined = new HashMap<>();

	private TokenTable() {
		final List<String[]> operandsCsv = new Inputer(OPERANDS_CSV_PATH).readAsQuartedCsv();
		initMap(operands, operandsCsv);

		final List<String[]> operatorsCsv = new Inputer(OPERATORS_CSV_PATH).readAsQuartedCsv();
		initMap(operators, operatorsCsv);

		final List<String[]> tokensCsv = new Inputer(TOKENS_CSV_PATH).readAsQuartedCsv();
		initMap(tokens, tokensCsv);
	}

	public static TokenTable getInstance() {
		return instance;
	}

	private void initMap(final Map<String, String> map, final List<String[]> csv){
		if(isValidCsv(csv)){
			csv.forEach(csvLine -> map.put(csvLine[0], csvLine[1]));
		} else {
			throw new LexerException("invalid csv");
		}
	}

	private boolean isValidCsv(final List<String[]> operandsCsv) {
		return operandsCsv
				.stream()
				.allMatch(TokenTable::isValidArray);
	}

	private static boolean isValidArray(final String[] csvLine) {
		if(csvLine.length != 2) {
			return false;
		}
		return Token.isValidKind(csvLine[1]);
	}


	public boolean isDefinedToken(final String name) {
		return operands.containsKey(name) || operators.containsKey(name) || tokens.containsKey(name) || userDefined.containsKey(name);
	}

	public boolean isDefinedOperands(final String name) {
		return operands.containsKey(name) || userDefined.containsKey(name);
	}

	public String getKind(final String kayName) {
		if(userDefined.containsKey(kayName)){
			return new String(userDefined.get(kayName));
		} else if(operands.containsKey(kayName)) {
			return new String(operands.get(kayName));
		} else if(operators.containsKey(kayName)) {
			return new String(operators.get(kayName));
		} else if(tokens.containsKey(kayName)){
			return new String(tokens.get(kayName));
		}else {
			throw new LexerException("undefined token: " + kayName);
		}
	}

	public boolean defineOperand(final String name, final String kind) {
		if(Token.isValidName(name) && Token.isValidKind(kind) && !userDefined.containsKey(name)) {
			userDefined.put(name, kind);
			return true;
		} else {
			return false;
		}
	}

	public boolean defineInference(final String name) {
		if(Token.isValidName(name) && !userDefined.containsKey(name)) {
			userDefined.put(name, Token.inferenceKind(name));
			return true;
		} else {
			return false;
		}
	}

	public boolean isDefinedVariable(final String name) {
		return isDefined(name, Token.VARIABLE);
	}

	public boolean isDefinedConstant(final String name) {
		return isDefined(name, Token.CONSTANT);
	}

	public boolean isDefinedFunction(final String name) {
		return isDefined(name, Token.FUNCTION);
	}

	public boolean isDefinedVariableOrConstant(final String name) {
		return isDefinedVariable(name) || isDefinedConstant(name);
	}

	public boolean isDefined(final String name, final String kind) {
		if(userDefined.containsKey(name)) {
			return userDefined.get(name).equals(kind);
		} else if(operands.containsKey(name)) {
			return operands.get(name).equals(kind);
		} else {
			return false;
		}
	}

	public List<String> getDefinedOperands() {
		final List<String> userDefined = this.userDefined.keySet()
				.stream()
				.map(String::new)
				.collect(Collectors.toList());
		final List<String> defined = operands.keySet()
				.stream()
				.map(String::new)
				.collect(Collectors.toList());

		final List<String> allDefined = new ArrayList<>();
		allDefined.addAll(userDefined);
		allDefined.addAll(defined);
		return allDefined;
	}

	public List<String> getDefinedConstants() {
		return getDefined(Token.CONSTANT);
	}

	public List<String> getDefinedVariables() {
		return getDefined(Token.VARIABLE);
	}

	public List<String> getDefinedFunctions() {
		return getDefined(Token.FUNCTION);
	}

	public List<String> getDefined(final String kind) {
		return getDefinedOperands()
				.stream()
				.filter(str -> !operands.get(str).equals(kind))
				.collect(Collectors.toList());
	}

}

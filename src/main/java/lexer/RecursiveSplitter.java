package lexer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import token.Token;

/**
 *
 * @author hayas
 * 前から見て最長一致をトークン化 変数、定数なら後ろの数字もまとめてトークン化
 * トークン化したらまた再帰的に処理
 *
 */

public class RecursiveSplitter {
	private final TokenTable tokenTable = TokenTable.getInstance();

	private final String splitted;
	private final RecursiveSplitter nextSplitter;

	public RecursiveSplitter(final String expression) {
		final String trimmed = expression.trim();
		this.splitted = split(trimmed);

		if(isSplitted()) {
			this.nextSplitter = new RecursiveSplitter(trimmed.substring(this.splitted.length()));
		} else {
			this.nextSplitter = null;
		}
	}


	public boolean isSplitted() {
		return !splitted.isEmpty();
	}

	public String splitted() {
		return splitted;
	}

	public RecursiveSplitter getNextSplitter() {
		return nextSplitter;
	}

	//TODO refactoring: don't use recursive call
	public List<String> getFollows() {
		final LinkedList<String> follows = new LinkedList<>();

		for(RecursiveSplitter splitter = this; splitter.isSplitted(); splitter = splitter.nextSplitter) {
			if(follows.size()>0 && tokenTable.isDefinedVariableOrConstant(follows.getLast()) && Token.isValidDigit(splitter.splitted())){
				follows.addLast(follows.removeLast() + splitter.splitted());
			} else {
				follows.addLast(splitter.splitted());
			}
		}

		return new ArrayList<>(follows);
	}

	private String split(final String expression) {
		return nextMatch(expression);
	}

	private String nextMatch(final String expression) {
		if(expression.matches("^\\d.*$")) {		//start with digit
			return splitDigit(expression);
		} else {
			return longestMatch(expression);
		}
	}

	private String longestMatch(final String expression) {
		final LinkedList<String> removed = new LinkedList<>(tokenTable.getDefinedAllTokens());
		final List<String> remover = removed
				.stream()
				.filter(ope -> !expression.startsWith(ope))
				.collect(Collectors.toList());

		removed.removeAll(remover);
		removed.sort((a,b) -> a.length() - b.length());

		if(removed.isEmpty()) {
			return expression;
		} else {
			return removed.getLast();
		}
	}

	private String splitDigit(final String expression) {
		if(expression.matches("^\\d.*$")) {		//start with digit
			return expression.split("(?<=\\d)(?=\\D)")[0];
		} else {
			return expression;
		}
	}
}

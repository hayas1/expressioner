package lexer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lexer.tokens.Token;

/**
 *
 * @author hayas
 * 前から見て最長一致をトークン化 変数、定数なら後ろの数字もまとめてトークン化
 * トークン化したらまた再帰的に処理
 *
 */

public class FactorSplitter {
	private final TokenTable tokenTable = TokenTable.getInstance();

	private final String splitted;
	private final FactorSplitter nextSplitter;

	public FactorSplitter(final String factors) {
		this.splitted = split(factors);

		if(isSplitted()) {
			this.nextSplitter = new FactorSplitter(factors.substring(this.splitted.length()));
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

	public FactorSplitter getNextSplitter() {
		return nextSplitter;
	}

	//TODO refactoring
	public List<String> getFollows() {
		final LinkedList<String> follows = new LinkedList<>();

		for(FactorSplitter splitter = this; splitter.isSplitted(); splitter = splitter.nextSplitter) {
			if(follows.size()>0 && tokenTable.isDefinedVariableOrConstant(follows.getLast()) && Token.isValidDigit(splitter.splitted())){
				follows.addLast(follows.removeLast() + splitter.splitted());
			} else {
				follows.addLast(splitter.splitted());
			}
		}

		return new ArrayList<>(follows);
	}

	private String split(final String factors) {
		return longestMatch(factors);
	}

	private String longestMatch(final String factors) {
		final LinkedList<String> removed = new LinkedList<>(tokenTable.getDefinedOperands());
		final List<String> remover = removed
				.stream()
				.filter(ope -> !factors.startsWith(ope))
				.collect(Collectors.toList());

		removed.removeAll(remover);
		removed.sort((a,b) -> a.length() - b.length());

		if(removed.isEmpty()) {
			return factors;
		} else {
			return removed.getLast();
		}
	}
}

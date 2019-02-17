package binding;

import java.util.HashMap;
import java.util.Map;

import token.Token;

public class Bindings {
	private final static Bindings instance = new Bindings();
	private final Map<Token, Binding> map = new HashMap<>();

	public static Bindings getInstance() {
		return instance;
	}

	public boolean put(final Token token, final Binding binding) {
		if(isDefined(token)) {
			return false;
		} else {
			map.put(token, binding);
			return true;
		}
	}

	public boolean isDefined(final Token token) {
		return map.containsKey(token);
	}

	public Binding getBinding(final Token token) {
		return map.get(token);
	}

	public Binding overPut(final Token token, final Binding binding) {
		return map.put(token, binding);
	}
}

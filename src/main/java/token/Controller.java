package token;

public class Controller extends Token {
	public static final String DEFVAR = "defvar";
	public static final String DEFCONS = "defcons";
	public static final String DEFFUNC = "deffunc";
	public static final String NEW = "new";
	public static final String COLON = ":";

	protected Controller(final String name) {
		super(name);
	}

	@Override
	public String getKind() {
		return Token.CONTROLLER;
	}

	@Override
	public boolean isDef() {
		return isDefVar() || isDefCons() || isDefFunc();
	}

	@Override
	public boolean isColon() {
		return super.getName().equals(Controller.COLON);
	}

	public static boolean isDef(final String token) {
		return isDefVar(token) || isDefCons(token) || isDefFunc(token);
	}

	public static boolean isDefVar(final String token) {
		return Controller.DEFVAR.equals(token);
	}

	public static boolean isDefCons(final String token) {
		return Controller.DEFCONS.equals(token);
	}

	public static boolean isDefFunc(final String token) {
		return Controller.DEFFUNC.equals(token);
	}

	public boolean isDefVar() {
		return super.getName().equals(Controller.DEFVAR);
	}

	public boolean isDefCons() {
		return super.getName().equals(Controller.DEFCONS);
	}
	public boolean isDefFunc() {
		return super.getName().equals(Controller.DEFFUNC);
	}

	public boolean isNew() {
		return super.getName().equals(Controller.NEW);
	}
}

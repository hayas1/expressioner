package tree;

import token.Paren;
import visitor.EtVisitor;

/**
 *
 * 括弧式 -> 左括弧 式 右括弧
 * @author hayas
 *
 */
public class ParenedExpression extends Factor {
	private Paren leftParen;
	private final static int MAIN_EXPRESSION = 0;
	private Paren rightParen;


	@Override
	public boolean accept(final EtVisitor visitor) {
		if(visitor.visit(this)) {
			getExpression().accept(visitor);
		}
		return visitor.leave(this);
	}

	@Override
	public ParenedExpression setParent(final EtNode parent) {
		return (ParenedExpression)super.setParent(parent);
	}

	public ParenedExpression setChildren(final Paren leftParen, final Expression expression, final Paren rightParen) {
		setLeftParen(leftParen);
		setExpression(expression);
		setRightParen(rightParen);

		return this;
	}

	@Override
	public String toString() {
		return getLeftParen().toString() + getExpression().toString() + getRightParen().toString();
	}

	@Override
	public ParenedExpression copySubEt(final EtNode parent) {
		final ParenedExpression parenedExpression = new ParenedExpression();

		final Paren leftParen = getLeftParen().clone();
		final Expression Expression = getExpression().copySubEt(parenedExpression);
		final Paren rightParen = getRightParen().clone();

		return parenedExpression.setParent(parent).setChildren(leftParen, Expression, rightParen);
	}

	public Paren getLeftParen() {
		return leftParen;
	}

	public ParenedExpression setLeftParen(final Paren leftParen) {
		if(leftParen.isLeftParen()) {
			this.leftParen = leftParen;
		} else {
			throw new NodeTypeException("not left paren token: " + leftParen.getName());
		}
		return this;
	}

	public Expression getExpression() {
		return (Expression)super.getChild(MAIN_EXPRESSION);
	}

	public ParenedExpression setExpression(final Expression expression) {
		if(expression != null) {
			super.setChild(MAIN_EXPRESSION, expression);
		} else {
			throw new NodeTypeException("don't allow null expression");
		}
		return this;
	}

	public Paren getRightParen() {
		return rightParen;
	}

	public ParenedExpression setRightParen(final Paren rightParen) {
		if(rightParen.isRightParen()) {
			this.rightParen = rightParen;
		} else {
			throw new NodeTypeException("not right paren token: " + leftParen.getName());
		}
		return this;
	}


	/**
	 * 子を1つしか持たないノードのうち最もルートに近いものを取得する
	 * @return 子を1つしか持たないノードのうち最もルートに近いもの
	 */
	public EtNode getParentElement() {
		EtNode upper;
		for(upper = this; upper.getParent()!=null; upper = upper.getParent()) {
			final EtNode parent = upper.getParent();
			if(parent.getChildren().size()>1) {
				return upper;
			}
		}
		return upper;
	}

	/**
	 * 子を1つしか持たないノードはスキップして探索し、初めて2つ以上の子を持ったノードを発見したときそのノードを取得する
	 * @return 初めて子を2つ以上もつノード
	 */
	public EtNode getChildElement() {
		EtNode downer;
		for(downer = this; !downer.getChildren().isEmpty(); downer = downer.getChild(0)) {
			if(downer.getChildren().size()>1) {
				return downer;
			} else if(downer.isSameNodeType(new Expressions())) {
				continue;
			}
		}
		return downer;
	}

	public boolean parenRemovable() {
		return getParentElement().isSameNodeType(getChildElement());
	}





}

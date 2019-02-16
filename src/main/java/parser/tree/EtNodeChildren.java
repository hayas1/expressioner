package parser.tree;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EtNodeChildren extends AbstractList<EtNode> {
	private final List<EtNode> nodes;

	public EtNodeChildren() {
		this.nodes = new ArrayList<>();
	}

	public EtNodeChildren(final EtNode... nodes) {
		this.nodes = Arrays.asList(nodes);
	}

	@Override
	public EtNode get(int index) {
		return nodes.get(index);
	}

	@Override
	public int size() {
		return nodes.size();
	}

	@Override
	public void add(final int index, final EtNode element) {
		nodes.add(index, element);
	}

	@Override
	public EtNode set(final int index, final EtNode element) {
		return nodes.set(index, element);
	}


	/**
	 * リスト内にtargetがあればelementで置き換える。返り値はtargetがあったインデックス。なかった場合は-1
	 * @param target 置き換えられる要素
	 * @param element 置き換える要素
	 * @return targetがあったインデックス。なかった場合は-1
	 */
	public int replace(final EtNode target, final EtNode element) {
		if(super.contains(target)) {
			final int index = indexOf(target);
			super.set(index, element);
			return index;
		} else {
			return -1;
		}
	}





}

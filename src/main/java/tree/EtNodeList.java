package tree;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EtNodeList extends AbstractList<EtNode> {
	private final List<EtNode> nodes;

	public EtNodeList() {
		this.nodes = new ArrayList<>();
	}

	public EtNodeList(final EtNode... nodes) {
		this.nodes = Arrays.asList(nodes);
	}

	@Override
	public EtNode get(int index) {
		return nodes.get(index);
	}

	@Override
	public int size() {
		return (int)nodes
				.stream()
				.filter(node -> node!=null)
				.count();
	}

	@Override
	public void add(final int index, final EtNode element) {
		nodes.add(index, element);
	}

	@Override
	public EtNode set(final int index, final EtNode element) {
		if(index == nodes.size()) {
			nodes.add(element);
			return null;
		} else {
			return nodes.set(index, element);
		}
	}

	@Override
	public String toString() {
		return nodes
				.stream()
				.map(EtNode::toString)
				.collect(Collectors.joining());
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
			set(index, element);
			return index;
		} else {
			return -1;
		}
	}

	public <T extends EtNode> List<T> downCast(final Class<T> nodeType){
		return nodes
				.stream()
				.map(node -> nodeType.cast(node))
				.collect(Collectors.toList());
	}





}

package taiga.parser;

import taiga.interfaces.Node;

public class PropertyHandle {
	public Node node;
	public Object propName;

	public PropertyHandle(Node node, Object propName) {
		this.node = node;
		this.propName = propName;
	}
}

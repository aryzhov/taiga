package taiga.parser;

import java.util.Iterator;
import java.util.NoSuchElementException;

import taiga.interfaces.Node;
import taiga.interfaces.Property;


class InstanceContext implements Iterable<Node> {

	public enum Mode {
		VALUE, LIST, MAP, DEFAULT
	}

	protected final InstanceContext parent;
	protected final Property node;
	public Mode instantiateMode = Mode.DEFAULT;
	
	public InstanceContext(InstanceContext parent, Property node) {
		super();
		this.parent = parent;
		this.node = node;
	}

	public InstanceContext(Mode mode, InstanceContext parent, Property node) {
		this.instantiateMode = mode;
		this.parent = parent;
		this.node = node;
	}
	
	public ValueHolder resolve(ParserContext pc) {
		if(pc instanceof ValueHolder)
			return (ValueHolder) pc;
		else if(parent != null)
			return parent.resolve(pc);
		else
			return null;
	}
	
	public Property getNode() {
		return node;
	}

	@Override
	public Iterator<Node> iterator() {
		return new NodeIterator(this);
	}

	public class NodeIterator implements Iterator<Node> {
		
		private InstanceContext next;
		
		private NodeIterator(InstanceContext ic) {
			super();
			this.next = ic;
		}

		@Override
		public boolean hasNext() {
			return next != null && next.node != null;
		}

		@Override
		public Node next() {
			if(next == null || next.node == null)
				throw new NoSuchElementException();
			Node result = (Node) next.node;
			while(next != null && next.node == result)
				next = next.parent;
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}

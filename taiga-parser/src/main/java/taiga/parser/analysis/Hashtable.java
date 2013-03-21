package taiga.parser.analysis;

import java.util.HashMap;
import java.util.Map;

public class Hashtable<K,V> extends HashMap<K, V> {

	public Hashtable() {
		super();
	}

	public Hashtable(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public Hashtable(int initialCapacity) {
		super(initialCapacity);
	}

	public Hashtable(Map<? extends K, ? extends V> m) {
		super(m);
	}
	
}

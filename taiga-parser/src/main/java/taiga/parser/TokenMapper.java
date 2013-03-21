package taiga.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import taiga.parser.analysis.ReversedDepthFirstAdapter;
import taiga.parser.node.Node;
import taiga.parser.node.Token;

public class TokenMapper extends ReversedDepthFirstAdapter {

	private Map<Node, Token> map = new HashMap<Node, Token>();
	
	private Token lastToken;
	
	@Override
	public void defaultCase(Node node) {
		lastToken = (Token) node;
	}
	
	@Override
	public void defaultOut(Node node) {
		if(!(node instanceof Token))
			map.put(node, lastToken);
	}

	public Map<Node, Token> getMap() {
		return Collections.unmodifiableMap(map);
	}
	
}

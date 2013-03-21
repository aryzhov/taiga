package taiga.parser;

import java.util.ArrayList;
import java.util.List;

import taiga.interfaces.Array;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.Node;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.node.AArrayBlock;
import taiga.parser.node.AObjectBlock;
import taiga.parser.node.PBlock;

class BlocksParserContext extends ParserContext {

	private final List<ParserContext> blocks;
	private final boolean objectBlocks;
	private final boolean arrayBlocks;
	
	public BlocksParserContext(ParserContext parent, List<PBlock> blocks, TypeScope typeScope) throws TaigaException {
		super(parent);
		boolean object = false;
		boolean array = false;
		this.blocks = new ArrayList<ParserContext>();
		for(PBlock block: blocks) {
			if(block instanceof AArrayBlock) {
				array = true;
				this.blocks.add(new ArrayBlockParserContext(parent, (AArrayBlock) block));
			}
			else if(block instanceof AObjectBlock) {
				object = true;
				this.blocks.add(new ObjectBlockParserContext(parent, (AObjectBlock) block, typeScope));
			}
		}
		this.objectBlocks = object;
		this.arrayBlocks = array;
	}

	public final boolean hasObjectBlocks() {
		return objectBlocks;
	}
	
	public final boolean hasArrayBlocks() {
		return arrayBlocks;
	}
	
	
	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		Property node = ic.getNode();
		if(node == null)
			return;
		if(arrayBlocks && !(node instanceof Array)) {
			logAndThrow(ErrorCode.NOT_ARRAY, node.getValueClass());
			return;
		}
		if(objectBlocks && !(node instanceof Node)) {
			logAndThrow(ErrorCode.NOT_OBJECT, node.getValueClass());
			return;
		}
		for(ParserContext pc: blocks) {
			pc.initialize(ic);
		}
	}

	@Override
	protected taiga.parser.node.Node getASTNode() {
		return blocks.isEmpty() ? null : blocks.get(0).getASTNode();
	}

	@Override
	public void init() throws TaigaException {
		for(ParserContext pc: blocks)
			pc.init();
	}

	@Override
	public boolean hasDependency(String typeName) {
		for(ParserContext pc: blocks)
			if(pc.hasDependency(typeName))
				return true;
		return false;
	}
	
}


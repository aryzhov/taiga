package taiga.parser.lexer;

import java.io.IOException;
import java.util.Stack;

import taiga.parser.node.TBlank;
import taiga.parser.node.TColon;
import taiga.parser.node.TComma;
import taiga.parser.node.TEol;
import taiga.parser.node.TEqual;
import taiga.parser.node.TFreeValue;
import taiga.parser.node.TInlineValue;
import taiga.parser.node.TLBrace;
import taiga.parser.node.TLBracket;
import taiga.parser.node.TLPar;
import taiga.parser.node.TQuestionMark;
import taiga.parser.node.TRBrace;
import taiga.parser.node.TRBracket;
import taiga.parser.node.TRPar;
import taiga.parser.node.TRightArrow;

public class MyLexer extends Lexer {

	private Stack<BlockState> stateStack = new Stack<BlockState>();
	private BlockType blockType = BlockType.NONE;
	private State valueState = State.FREE;
	private boolean blockStart = false;
	private boolean ignoreEol = true;
	
	public MyLexer(PushbackReader in) {
		super(in);
	}

	@Override
	protected void filter() throws LexerException, IOException {
		if(token instanceof TBlank) {
			// ignore
		} else if(token instanceof TEol) {
			if(ignoreEol) {
				this.token = null;
			} else if(blockStart) {
				valueState = State.FREE;
				blockStart = false;
			}
			ignoreEol = true;
		} else if(token instanceof TComma) {
			ignoreEol = true;
		} else {
			ignoreEol = false;
			blockStart = false;
			if(token instanceof TEqual) {
				state = valueState;
			} else if(token instanceof TInlineValue || token instanceof TFreeValue) {
				state = State.NORMAL;
			} else if(token instanceof TLBrace) {
				pushState(BlockType.BRACE);
				ignoreEol = true;
			} else if(token instanceof TLPar) {
				pushState(BlockType.PAREN);
				ignoreEol = true;
			} else if(token instanceof TLBracket) {
				pushState(BlockType.BRACKET);
				ignoreEol = true;
			} else if(token instanceof TRBrace) {
				popState(BlockType.BRACE);
			} else if(token instanceof TRPar) {
				popState(BlockType.PAREN);
			} else if(token instanceof TRBracket) {
				popState(BlockType.BRACKET);
			} else if(
					token instanceof TColon ||
					token instanceof TRightArrow ||
					token instanceof TQuestionMark) {
				ignoreEol = true;
			}
		}
	}
	
	private void popState(BlockType bt) {
		if(!stateStack.isEmpty() && this.blockType == bt) {
			BlockState bs = stateStack.pop();
			this.blockType = bs.blockType;
			this.valueState = bs.valueState;
		}
	}

	private void pushState(BlockType nextBlockType) {
		stateStack.push(new BlockState(valueState, blockType));
		this.blockType = nextBlockType;
		blockStart = true;
		valueState = State.INLINE;
	}
	
	private static class BlockState {
		public State valueState;
		public BlockType blockType;
		private BlockState(State vs, BlockType bt) {
			this.valueState = vs;
			this.blockType = bt;
		}
	}
	
	enum BlockType {
		NONE, BRACE, BRACKET, PAREN
	}
	
}

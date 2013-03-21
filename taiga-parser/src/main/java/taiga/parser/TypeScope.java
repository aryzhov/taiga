package taiga.parser;

import taiga.interfaces.TaigaException;

interface TypeScope {
	
	public void addTypeDef(TypeParserContext tpc) throws TaigaException;

}

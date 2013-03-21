package taiga.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.StringUtils;
import taiga.interfaces.TaigaException;


abstract class TypeScopeParserContext extends ParserContext implements TypeScope {

	private List<TypeParserContext> types = null;
	private Set<String> typeNames = null;
	
	protected TypeScopeParserContext(ParserContext parent) {
		super(parent);
	}

	protected TypeScopeParserContext(Owner owner) {
		super(owner);
	}

	public TypeScope getTypeScope() {
		return this;
	}
	
	@Override
	protected ParserContext doResolve(String name) {
		if(typeNames == null || !typeNames.contains(name))
			return null;		
		for(int i = types.size()-1; i >= 0; i--) {
			TypeParserContext tpc = types.get(i);
			if(StringUtils.equals(tpc.getTypeName(), name, false)) {
				assert tpc.isInitialized();
				return tpc;
			}
		}
		return null;
	}

	@Override
	public void init() throws TaigaException {
		if(types == null)
			return;
		if(types.size() == 1) {
			types.get(0).init();
			return;
		}
		int initialized = 0;
		while(initialized < types.size()) {
			TypeParserContext first = findNonInitialized(initialized);
			TypeParserContext tpc = first;
			TypeParserContext dep;
			do {
				dep = findFirstUnresolvedDependency(tpc);
				if(dep == null)
					break;
				else
					tpc = dep;
			} while (dep != first);
			if(dep != null) {
				// TODO: recreate the chain
				logAndThrow(ErrorCode.CIRCULAR_DEPENDENCY, first.getTypeName());
				break;
			} else {
				tpc.init();
				initialized++;
			}
		}
	}

	private TypeParserContext findFirstUnresolvedDependency(TypeParserContext tpc) {
		for(int i = types.size()-1; i >= 0; i--) {
			TypeParserContext tpc2 = types.get(i);
			if(tpc2 != tpc && !tpc2.isInitialized() && tpc.hasDependency(tpc2.getTypeName()))
				return tpc2;
		}
		return null;
	}

	private TypeParserContext findNonInitialized(int seed) {
		int n = types.size();
		int first = (seed + n + n / 2) % n; 
		for(int i = first; i < n; i++) {
			TypeParserContext tpc = types.get(i);
			if(!tpc.isInitialized())
				return tpc;
		}
		for(int i = 0; i < first; i++) {
			TypeParserContext tpc = types.get(i);
			if(!tpc.isInitialized())
				return tpc;
		}
		assert false;
		return null;
	}

	@Override
	public void addTypeDef(TypeParserContext tpc) throws TaigaException {
		if(types == null) {
			types = new ArrayList<TypeParserContext>();
			typeNames = new HashSet<String>();
		}
		String typeName = tpc.getTypeName();
		if(typeNames.contains(typeName)) {
			tpc.logAndThrow(ErrorCode.DUPLICATE_TYPE, tpc.getTypeName());
			return;
		}
		types.add(tpc);
		typeNames.add(tpc.getTypeName());
	}
	
	@Override
	public boolean hasDependency(String typeName) {
		if(typeNames == null || typeNames.contains(typeName))
			return false;
		for(int i = types.size()-1; i >= 0; i--)
			if(types.get(i).hasDependency(typeName))
				return true;
		return false;
	}
	
}

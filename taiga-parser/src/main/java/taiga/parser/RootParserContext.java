package taiga.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.InstanceContext.Mode;
import taiga.parser.node.Node;
import taiga.parser.node.Start;

public class RootParserContext extends ParserContext {

	private final class AttributeValuesInstanceContext extends InstanceContext {
		private final Map<String, Object> attributeValues;

		public AttributeValuesInstanceContext(Mode mode, Map<String, Object> attributeValues) {
			this(mode, attributeValues, null);
		}

		public AttributeValuesInstanceContext(Mode mode, Map<String, Object> attributeValues, Property node) {
			super(mode, null, node);
			this.attributeValues = attributeValues;
		}

		@Override
		public ValueHolder resolve(ParserContext pc) {
			if(pc instanceof AttributeParserContext) {
				String attrName = ((AttributeParserContext)pc).getAttributeName();
				if(attributeValues != null && attributeValues.containsKey(attrName))
					return new AttributeValueHolder(attrName, attributeValues.get(attrName));
			}
			return null;
		}
	}

	private final List<FileParserContext> files = new ArrayList<FileParserContext>();
	private Model defaultModel = null;
	private Map<String, AttributeParserContext> attributes;
	
	protected RootParserContext(Owner owner) {
		super(owner);
		attributes = new HashMap<String, AttributeParserContext>();
	}

	@Override
	public Model getModel() {
		return defaultModel;
	}
	
	@Override
	protected Node getASTNode() {
		return null;
	}

	public void addFile(String fn, Start ast) throws TaigaException {
		if(files.isEmpty())
			try {
				defaultModel = owner.getDefaultModel();
			} catch (ModelException e) {
				throw new TaigaException(logAndThrow(e));
			}			
		FileParserContext fpc = new FileParserContext(this, fn, ast.getPRoot(), defaultModel);
		fpc.init();
		files.add(fpc);		
	}

	@Override
	protected ParserContext resolve(String name, NameType nameType) {
		for(FileParserContext fpc: files) {
			ParserContext pc = fpc.doResolve(name);
			if(pc != null)
				return pc;
		}
		if(nameType == NameType.VALUE)
			return getOrCreateGlobalAttribute(name);
		else
			return super.resolve(name, nameType);
	}

	private ParserContext getOrCreateGlobalAttribute(String name) {
		AttributeParserContext pc = attributes.get(name);
		if(pc == null) {
			pc = new AttributeParserContext(this, name);
			attributes.put(name, pc);
		}
		return pc;
	}

	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		if(files.isEmpty()) {
			logAndThrow(ErrorCode.NO_SOURCE_LOADED);
			return null;
		} 
		
		Property node = null;
		boolean first = true;
		for(FileParserContext file: files) {
			if(first) {
				ValueHolder vh = file.instantiate(ic);
				if(files.size() == 1)
					return vh; 
				node = vh.getProperty(Object.class);
				first = false;
 			} else {
 				file.initialize(new InstanceContext(ic.instantiateMode, ic, node));
 			}
		}
		return node == null ? null : new PropertyValueHolder(node);
	}
	
	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		for(FileParserContext fpc: files) { 
			fpc.initialize(ic);
		}
	}

	void initialize(Object obj, final Map<String, Object> attributeValues) throws TaigaException {
		if(files.isEmpty()) {
			logAndThrow(ErrorCode.NO_SOURCE_LOADED);
			return;
		} 
		if(obj == null) {
			logAndThrow(ErrorCode.CANNOT_INITIALIZE_NULL);
			return;
		}
		FileParserContext file = files.get(0);
		Property node = file.createNodeForObject(obj);
		if(node != null) {
			initialize(new AttributeValuesInstanceContext(Mode.VALUE, attributeValues, node));
		}
	}
	
	@Override
	public String getFileName() {
		return null;
	}

	ValueHolder instantiate(Mode mode, final Map<String, Object> attributeValues) throws TaigaException {
		return instantiate(new AttributeValuesInstanceContext(mode, attributeValues));
	}

//	public final ValueHolder instantiate(Class<?> expectedClass, Map<String, Object> attributeValues) throws TaigaException {
//		ValueHolder vh = instantiate(attributeValues);
//		if(StringUtils.isEmpty(path))
//			return vh;
//		else {
//			Property p = vh.getProperty(expectedClass);
//			if(!(p instanceof Node)) {
//				logAndThrow(ErrorCode.NOT_NODE, path);
//				return null;
//			}
//			try {
//				return new PropertyValueHolder(((taiga.interfaces.Node)p).getProperty(path));
//			} catch (ModelException e) {
//				logAndThrow(e);
//				return null;
//			}
//		}
//	}

	public <T> T createValue(Map<String, Object> attributeValues, Class<T> expectedClass, Object... path) throws TaigaException {
		try {
			return createNode(attributeValues, expectedClass, path).getValue(expectedClass);
		} catch (ModelException e) {
			throw new TaigaException(logAndThrow(e));
		}
	}
	
	public Property createNode(Map<String, Object> attributeValues, Class<?> expectedClass, Object... path) throws TaigaException {
		ValueHolder vh = instantiate(path.length == 0 ? Mode.VALUE : Mode.MAP, attributeValues);
		if(vh == null)
			return null;
		if(path.length == 0)
			return vh.getProperty(expectedClass);
		Property prop = vh.getProperty(Object.class);
		for(int k = 0; k < path.length; k++) {
			Object propName = path[k];
			Class<?> cl = k == path.length -1 ? expectedClass : Object.class;
			if(!(prop instanceof taiga.interfaces.Node)) {
				throw new TaigaException(logAndThrow(ErrorCode.NOT_NODE, propName));
			}
			taiga.interfaces.Node node = (taiga.interfaces.Node)prop;
			try {
				prop = node.getProperty(propName);
			} catch (ModelException e) {
				throw new TaigaException(logAndThrow(e));
			}
		}
		return prop;
	}

	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}

	@Override
	public boolean hasDependency(String typeName) {
		return false;
	}

	@Override
	public void init() throws TaigaException {
	}

	
}

package taiga.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ErrorLog;
import taiga.interfaces.ErrorMessage;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Position;
import taiga.interfaces.ResourceProvider;
import taiga.interfaces.TaigaException;
import taiga.model.CoreModelFactory;
import taiga.parser.InstanceContext.Mode;
import taiga.parser.ParserContext.Owner;
import taiga.parser.lexer.Lexer;
import taiga.parser.lexer.LexerException;
import taiga.parser.lexer.MyLexer;
import taiga.parser.lexer.PushbackReader;
import taiga.parser.node.Start;
import taiga.parser.parser.Parser;
import taiga.parser.parser.ParserException;

public class TaigaEngine {
	
	private final RootParserContext root;
	private ErrorLog errorLog = new ErrorLog();
	private ModelFactory modelFactory = new CoreModelFactory(null);
	
	public TaigaEngine() {
		root = new RootParserContext(new OwnerImpl());
	}
	
	public ModelFactory getModelFactory() {
		return modelFactory;
	}
	
	public TaigaEngine setModelFactory(ModelFactory modelFactory) {
		this.modelFactory = modelFactory;
		return this;
	}
		
	public ErrorLog getErrorLog() {
		return errorLog;
	}

	public TaigaEngine setErrorLog(ErrorLog errorLog) {
		this.errorLog = errorLog;
		return this;
	}

	public TaigaEngine read(ResourceProvider rp) throws TaigaException {
		String fn = rp.getRelativeName();
		try {
			Lexer lexer = new MyLexer(new PushbackReader(rp.read()));
			Parser p = new Parser(lexer);
			Start ast = p.parse();
			root.addFile(fn, ast);
		} catch (ParserException e) {
			Position pos = e.getToken() != null ? new Position(fn, e.getToken().getLine(), e.getToken().getPos()) : Position.UNDEFINED;
			errorLog.addAndThrow(new ErrorMessage(ErrorCode.SYNTAX_ERROR, pos, e.getToken() != null ? e.getToken().getText() : null, e, e.getMessage()));
		} catch (LexerException e) {
			errorLog.addAndThrow(new ErrorMessage(ErrorCode.SYNTAX_ERROR, new Position(fn), null, e, e.getMessage()));
		} catch (IOException e) {
			errorLog.addAndThrow(new ErrorMessage(ErrorCode.IO_EXCEPTION, new Position(fn), null, e, fn, e.getMessage()));
		} catch (ModelException e) {
			errorLog.addAndThrow(new ErrorMessage(e.getErrorCode(), new Position(fn), null, e, e.getParams()));
		}
		return this;
	}

	
	
	/**
	 * Creates and initializes the object specified by the loaded resources.
	 * If there is no root value defined, returns a map of name/value pairs
	 * cotaining top-level objects. The root value can be a simple constant,
	 * an anonymous JSON object, or an anonymous array.
	 */
	public Object instantiate() throws TaigaException {
		return instantiate(Object.class, null);
	}
	
	
	/**
	 * This is a type-safe version of #instantiate(). It will throw a TaigaException
	 * if the defined value isn't an instance of the expected class.
	 */
	public <T> T instantiate(Class<T> expectedClass) throws TaigaException {
		return instantiate(expectedClass, null);
	}

	
	private class OwnerImpl implements Owner {

		@Override
		public ErrorLog getErrorLog() {
			return errorLog;
		}

		@Override
		public Model getModel(String name) throws ModelException {
			return modelFactory.createModel(name);
		}

		@Override
		public Model getDefaultModel() throws ModelException {
			return modelFactory.getDefaultModel();
		}

	}

	public Object instantiate(Map<String, Object> attributeValues) throws TaigaException {
		return instantiate(Object.class, attributeValues);
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiate(Class<T> expectedClass, Map<String, Object> attributeValues) throws TaigaException {
		ValueHolder vh = root.instantiate(Mode.VALUE, attributeValues);
		if(vh == null)
			return null;
		else
			return (T) vh.getValue(expectedClass);
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiate(Class<T> expectedClass, Map<String, Object> attributeValues, Object... path) throws TaigaException {
		return root.createValue(attributeValues, expectedClass, path);
	}

	public Set<String> getAttributeNames() {
		return root.getAttributeNames();
	}

	public void initialize(Object obj) throws TaigaException {
		root.initialize(obj, null);
	}
	
	public void initialize(Object obj, Map<String, Object> attributeValues) throws TaigaException {
		root.initialize(obj, attributeValues);
	}

	public void read(String s) throws TaigaException {
		read(new StringResourceProvider(s));		
	}

	public Map<?, ?> instantiateMap() throws TaigaException {
		return instantiateMap(null);
	}
	
	public Map<?, ?> instantiateMap(Map<String, Object> attributeValues) throws TaigaException {
		ValueHolder vh = root.instantiate(Mode.MAP, attributeValues);
		if(vh == null)
			return null;
		else
			return (Map<?, ?>) vh.getValue(Map.class);
	}

	public Collection<?> instantiateList() throws TaigaException {
		return instantiateList(null);
	}

	public Collection<?> instantiateList(Map<String, Object> attributeValues) throws TaigaException {
		ValueHolder vh = root.instantiate(Mode.LIST, attributeValues);
		if(vh == null)
			return null;
		else
			return (List<?>) vh.getValue(List.class);
	}
}

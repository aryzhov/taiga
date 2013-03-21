package taiga.parser;

import static taiga.parser.ValueHolder.ERROR;

import java.util.HashSet;
import java.util.Set;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ErrorLog;
import taiga.interfaces.ErrorMessage;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Operator;
import taiga.interfaces.Position;
import taiga.interfaces.TaigaException;
import taiga.parser.node.AAddValue;
import taiga.parser.node.ABooleanConstant;
import taiga.parser.node.AConstIdent;
import taiga.parser.node.AConstantValue;
import taiga.parser.node.ADivValue;
import taiga.parser.node.AFreeValue;
import taiga.parser.node.AIdIdent;
import taiga.parser.node.AInlineValue;
import taiga.parser.node.ALoopValue;
import taiga.parser.node.AMulValue;
import taiga.parser.node.ANegValue;
import taiga.parser.node.ANullConstant;
import taiga.parser.node.ANumberConstant;
import taiga.parser.node.AObject;
import taiga.parser.node.AObjectValue;
import taiga.parser.node.ARangeValue;
import taiga.parser.node.ASelect;
import taiga.parser.node.ASelectValue;
import taiga.parser.node.AStringConstant;
import taiga.parser.node.ASubValue;
import taiga.parser.node.ATrueBoolean;
import taiga.parser.node.PConstant;
import taiga.parser.node.PIdent;
import taiga.parser.node.PValue;
import taiga.parser.node.TFreeValue;
import taiga.parser.node.TId;
import taiga.parser.node.TInlineValue;
import taiga.parser.node.TNumber;
import taiga.parser.node.TString;
import taiga.parser.node.Token;

abstract class ParserContext {
	
	protected final ParserContext parent;
	protected final Owner owner;
	
	protected ParserContext(ParserContext parent) {
		this.parent = parent;
		this.owner = parent.owner;
	}

	protected ParserContext(Owner owner) {
		this.parent = null;
		this.owner = owner;
	}
	
	public Owner getOwner() {
		return owner;
	}
	
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		logAndThrow(ErrorCode.NOT_SUPPORTED);
		return null;
	}

	public void initialize(InstanceContext ic) throws TaigaException {
		logAndThrow(ErrorCode.NOT_SUPPORTED);
	}

	public String getFileName() {
		return parent.getFileName();
	}

	// TODO: process escape sequences
	protected String parseString(String text) {
		return text;
	}

	protected Number parseNumber(String str, Class<?> expectedClass) {
		if(!str.matches("-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"))
			return null;
		try {
			if(expectedClass.equals(Integer.class) || expectedClass.equals(Integer.class))
				return Integer.parseInt(str);
			else if(expectedClass.equals(Long.class) || expectedClass.equals(Long.class))
				return Long.parseLong(str);
			else if(expectedClass.equals(Double.class) || expectedClass.equals(Double.TYPE))
				return Double.parseDouble(str);
			else if(expectedClass.equals(Float.class) || expectedClass.equals(Float.TYPE))
				return Float.parseFloat(str);
			else if(expectedClass.equals(Byte.class) || expectedClass.equals(Byte.TYPE))
				return Byte.parseByte(str);
			else if(expectedClass.equals(Short.class) || expectedClass.equals(Short.TYPE))
				return Short.parseShort(str);
			else {
				if(str.contains(".") || str.contains("e") || str.contains("E"))
					return Double.parseDouble(str);
				else
					return Integer.parseInt(str);
			}
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	protected ErrorMessage logAndThrow(taiga.parser.node.Node node, ErrorCode errorCode, Object... params) throws TaigaException {
		ErrorMessage result = new ErrorMessage(errorCode, getPosition(node), getText(node), null, params);
		owner.getErrorLog().addAndThrow(result);
		return result;
	}

	protected ErrorMessage logAndThrow(ErrorCode errorCode, Object... params) throws TaigaException {
		return logAndThrow(getASTNode(), errorCode, params);
	}
	
	protected ErrorMessage logAndThrow(ModelException ex) throws TaigaException {
		return logAndThrow(ex, getASTNode());
	}
	
	protected ErrorMessage logAndThrow(ModelException ex, taiga.parser.node.Node node) throws TaigaException {
		ErrorMessage result = new ErrorMessage(ex.getErrorCode(), getPosition(node), getText(node), ex, ex.getParams());
		owner.getErrorLog().addAndThrow(result);
		return result;
	}
	
	public Model getModel() {
		return parent.getModel();
	}
	
	public Position getPosition(taiga.parser.node.Node node) {
		Token token = getFirstToken(node);
		if(token != null)
			return new Position(getFileName(), token.getLine(), token.getPos());
		else
			return new Position(getFileName(), -1, -1);
	}


	protected Token getFirstToken(taiga.parser.node.Node node) {
		if(node instanceof Token)
			return (Token)node;
		else if(parent != null)
			return parent.getFirstToken(node);
		else
			return null;
	}
	
	protected String getText(taiga.parser.node.Node node) {
		Token token = getFirstToken(node);
		return token != null ? token.getText() : null;
	}

	protected abstract taiga.parser.node.Node getASTNode();

	protected ParserContext resolve(String name, NameType nameType) {
		ParserContext pc = doResolve(name);
		if(pc != null)
			return pc;
		if(parent != null)
			return parent.resolve(name, nameType);
		else
			return null;
	}
	
	protected ParserContext doResolve(String name) {
		return null;
	}

	public abstract boolean hasDependency(String typeName);
	
	/** The parser context is supposed to call the init() method of all its children */ 
	public abstract void init() throws TaigaException;

	/** Pass null type scope to create a type scope */
	protected ParserContext createValueParserContext(PValue value) throws TaigaException {
		if(value instanceof AConstantValue) {
			PConstant constant = ((AConstantValue)value).getConstant();
			return new ConstantParserContext(this, constant);
		} else if(value instanceof AObjectValue) {
			AObject object = (AObject) ((AObjectValue)value).getObject();
			return new ObjectParserContext(this, object);
		} else if(value instanceof AFreeValue) {
			TFreeValue v = (TFreeValue) ((AFreeValue)value).getFreeValue();
			return new FreeValueParserContext(this, v);
		} else if(value instanceof AInlineValue) {
			TInlineValue v = (TInlineValue) ((AInlineValue)value).getInlineValue();
			return new InlineValueParserContext(this, v);
		} else if(value instanceof ASelectValue) {
			ASelect v = (ASelect) ((ASelectValue)value).getSelect();
			return new SelectValueParserContext(this, v);
		} else if(value instanceof AAddValue) {
			PValue a = ((AAddValue)value).getA(); 
			PValue b = ((AAddValue)value).getB(); 
			return new OperatorParserContext(this, value, a, b, Operator.ADD);
		} else if(value instanceof ASubValue) {
			PValue a = ((ASubValue)value).getA(); 
			PValue b = ((ASubValue)value).getB(); 
			return new OperatorParserContext(this, value, a, b, Operator.SUB);
		} else if(value instanceof AMulValue) {
			PValue a = ((AMulValue)value).getA(); 
			PValue b = ((AMulValue)value).getB(); 
			return new OperatorParserContext(this, value, a, b, Operator.MUL);
		} else if(value instanceof ADivValue) {
			PValue a = ((ADivValue)value).getA(); 
			PValue b = ((ADivValue)value).getB(); 
			return new OperatorParserContext(this, value, a, b, Operator.DIV);
		} else if(value instanceof ANegValue) {
			PValue a = ((ANegValue)value).getValue(); 
			return new OperatorParserContext(this, value, a, null, Operator.NEG);
		} else if(value instanceof ALoopValue) {
			ALoopValue a = ((ALoopValue)value);
			return new LoopParserContext(this, a);
		} else if(value instanceof ARangeValue) {
			ARangeValue range = ((ARangeValue)value);
			return new RangeParserContext(this, range);
		} else {
			throw new TaigaException(logAndThrow(ErrorCode.NOT_IMPLEMENTED));
		}
	}

	protected Object parseValue(String text, Class<?> expectedClass) throws ModelException {
		text = text.trim();
		if(expectedClass == String.class)
			return parseString(text);
		else if(isNumber(expectedClass)) {
			Number num = parseNumber(text, expectedClass);
			if(num != null)
				return num;
			throw new ModelException(ErrorCode.INVALID_NUMBER, num);
		} else if(Boolean.class.equals(expectedClass) || Boolean.TYPE.equals(expectedClass)) {
			Boolean bool = parseBoolean(text);
			if(bool != null)
				return bool;
			throw new ModelException(ErrorCode.INVALID_BOOLEAN, text);
		} else if(expectedClass.equals(Object.class)) {
			return parseObject(text);
		} else { 
			return getModel().parseValue(text, expectedClass);
		}
	}

	protected boolean isNumber(Class<?> expectedClass) {
		return NUMBER_TYPES.contains(expectedClass);
	}
	
	private Object parseObject(String text) {
		Object o = parseBoolean(text);
		if(o == null) {
			o = parseNumber(text, Number.class);
			if(o == null)
				o = parseString(text);
		}
		return o;
	}

	private Boolean parseBoolean(String text) {
		if("true".equals(text))
			return Boolean.TRUE;
		else if("false".equals(text))
			return Boolean.FALSE;
		else
			return null;
	}

	// TODO: move to model?
	private Object parseEnum(String s, Class<?> expectedClass) throws ModelException {
		for(Object e: expectedClass.getEnumConstants()) {
			Enum<?> en = (Enum<?>) e;
			if(en.name().equals(s))
				return en;
		}
		throw new ModelException(ErrorCode.INVALID_ENUM_VALUE, expectedClass, s);
	}

	
	protected boolean isAssignableFrom(Class<?> dest, Object src) throws TaigaException {
		return getModel().isAssignableFrom(dest, src);
	}

	protected String parseId(TId id) {
		return id == null ? null : id.getText();
	}

	protected Object identToString(Node parent, PIdent ident, Class<?> expectedClass) throws TaigaException {
		if(ident instanceof AIdIdent) {
			try {
				String propName = parseId(((AIdIdent)ident).getId());
				Object prop = parent.getModel().parsePropertyName(parent, propName);
				if(prop == null) {
					logAndThrow(ErrorCode.PROPERTY_NOT_FOUND, propName);
					return ERROR;
				}
				return prop;
			} catch (ModelException e) {
				logAndThrow(e, ident);
				return ERROR;
			}
		} else if(ident instanceof AConstIdent) {
			return parseConstant(((AConstIdent)ident).getConstant(), expectedClass);
		}
		else {
			logAndThrow(ErrorCode.NOT_SUPPORTED, ident);
			return ERROR;
		}
	}

	protected Object parseConstant(PConstant constant, Class<?> expectedClass) throws TaigaException {
		Object result;
		if(constant instanceof ABooleanConstant) {
			result = (((ABooleanConstant)constant).getBoolean() instanceof ATrueBoolean);
		} else if(constant instanceof ANumberConstant) {
			TNumber num = ((ANumberConstant) constant).getNumber();
			result = parseNumber(num.getText().trim(), expectedClass);
			if(result == null)
				result = ERROR;
		} else if(constant instanceof AStringConstant) {
			TString str = ((AStringConstant)constant).getString();
			String text = str.getText().substring(1, str.getText().length()-1);
			result = parseString(text);
		} else if(constant instanceof ANullConstant) {
			result = null;
		} else {
			logAndThrow(ErrorCode.NOT_IMPLEMENTED);
			result = ERROR;
		}
		if(!isAssignableFrom(expectedClass, result)) {
			logAndThrow(ErrorCode.INCOMPATIBLE_TYPES, expectedClass, result == null ? null : result.getClass());
			return ERROR;
		} else {
			return result;
		}
	}
	
	static interface Owner {
		public ErrorLog getErrorLog();
		public Model getDefaultModel() throws ModelException;
		public Model getModel(String name) throws ModelException;
	}
	
	private static final Set<Class<?>> NUMBER_TYPES;
	
	static {
		NUMBER_TYPES = new HashSet<Class<?>>();
		NUMBER_TYPES.add(Number.class);
		NUMBER_TYPES.add(Integer.class);
		NUMBER_TYPES.add(Integer.TYPE);
		NUMBER_TYPES.add(Long.class);
		NUMBER_TYPES.add(Long.TYPE);
		NUMBER_TYPES.add(Double.class);
		NUMBER_TYPES.add(Double.TYPE);
		NUMBER_TYPES.add(Float.class);
		NUMBER_TYPES.add(Float.TYPE);
		NUMBER_TYPES.add(Byte.class);
		NUMBER_TYPES.add(Byte.TYPE);
		NUMBER_TYPES.add(Short.class);
		NUMBER_TYPES.add(Short.TYPE);
	}
	
	enum NameType {
		TYPE, VALUE
	}
}

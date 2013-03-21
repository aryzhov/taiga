package taiga.parser;

import java.util.ArrayList;
import java.util.List;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.InstanceContext.Mode;
import taiga.parser.node.AArrayBlock;
import taiga.parser.node.AAssignment;
import taiga.parser.node.AAssignmentStatement;
import taiga.parser.node.AInitialization;
import taiga.parser.node.AInitializationStatement;
import taiga.parser.node.AName;
import taiga.parser.node.ATypeDef;
import taiga.parser.node.ATypeDefStatement;
import taiga.parser.node.AValueStatement;
import taiga.parser.node.PIdent;
import taiga.parser.node.PStatement;
import taiga.parser.node.PValue;

public class StatementsParserContext extends ParserContext {


	private List<Statement> statements = new ArrayList<Statement>();
	private ArrayBlockParserContext valuesParserContext = null;
	private taiga.parser.node.Node astNode; 
	
	protected StatementsParserContext(ParserContext parent, List<PStatement> statements, TypeScope typeScope) throws TaigaException {
		super(parent);
		
		List<PValue> values = new ArrayList<PValue>();
		boolean hasAssignments = false;
		for(PStatement st: statements) {
			if(astNode == null)
				astNode = st;
			if(st instanceof ATypeDefStatement) {
				ATypeDefStatement tdst = (ATypeDefStatement) st;
				TypeParserContext tpc = new TypeParserContext(this, (ATypeDef) tdst.getTypeDef());
				typeScope.addTypeDef(tpc);
			} else if(st instanceof AAssignmentStatement) {
				AAssignment ass = (AAssignment)(((AAssignmentStatement)st).getAssignment());
				if(!values.isEmpty())
					logAndThrow(ass, ErrorCode.CANNOT_MIX_ASSIGNMENTS_AND_VALUES);
				ParserContext pc = createValueParserContext(ass.getValue());
				this.statements.add(new Statement(Op.ASSIGN, (AName) ass.getName(), pc));
				hasAssignments = true;
			} else if(st instanceof AInitializationStatement) {
				AInitialization init = (AInitialization) ((AInitializationStatement)st).getInitialization();
				InitializationParserContext pc = new InitializationParserContext(this, init); 
				this.statements.add(new Statement(Op.INIT, (AName) init.getName(), pc));
				hasAssignments = true;
			} else if(st instanceof AValueStatement) {
				PValue av = (PValue) ((AValueStatement)st).getValue();
				if(hasAssignments)
					logAndThrow(av, ErrorCode.CANNOT_MIX_ASSIGNMENTS_AND_VALUES);
				values.add(av);
			} else {
				logAndThrow(ErrorCode.NOT_IMPLEMENTED);
			}
		}
		if(!values.isEmpty()) {
			AArrayBlock dummyBlock = new AArrayBlock(values);
			valuesParserContext = new ArrayBlockParserContext(this, dummyBlock);
		}
	}

	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		switch(ic.instantiateMode) {
		case VALUE:
			if(valuesParserContext == null) {
				logAndThrow(ErrorCode.VALUE_NOT_DEFINED);
				return null;
			} else {
				return valuesParserContext.instantiate(ic);
			}
		case LIST:
			if(valuesParserContext == null) {
				try {
					Property result = getModel().createAnonymousArrayNode();
					result.init(new EmptyParams(), ic);
					return new PropertyValueHolder(result);
				} catch (ModelException e) {
					logAndThrow(e);
					return null;
				}
			} else {
				return valuesParserContext.instantiate(ic);
			}
		default:
			if(valuesParserContext != null && ic.instantiateMode != Mode.MAP) 
				return valuesParserContext.instantiate(ic);
			if(ic.instantiateMode != Mode.MAP) {
				logAndThrow(new ModelException(ErrorCode.VALUE_NOT_DEFINED));
				return null;
			}
			Property result; 
			try {
				result = getModel().createAnonymousObjectNode();
				result.init(new EmptyParams(), ic);
			} catch (ModelException e) {
				logAndThrow(e);
				return null;
			}
			initialize(new InstanceContext(ic, result));
			return new PropertyValueHolder(result);
		}
	}

	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		switch(ic.instantiateMode) {
		case VALUE:
			if(valuesParserContext == null) {
				logAndThrow(ErrorCode.VALUE_NOT_DEFINED);
				return;
			}
		case LIST:
			if(valuesParserContext != null) {
				valuesParserContext.initialize(ic);
			}
			return;
		default:
			Property prop = ic.getNode();
			if(prop == null)
				return;
			if(!(prop instanceof Node)) {
				logAndThrow(ErrorCode.NOT_NODE, prop.getName());
				return;
			}
			Node node = (Node) prop;
			for(Statement st: statements) {
				switch(st.op) {
				case ASSIGN: {
					PropertyHandle ph = parseName(node, st.name);
					if(ph != null) {
						try {
							ValueHolder vh = st.pc.instantiate(ic); 
							if(vh == null)
								break;
							Class<?> cl = ph.node.getPropertyBaseClass(ph.propName);
							if(cl == null) {
								logAndThrow(ErrorCode.PROPERTY_NOT_FOUND, ph.propName);
								break;
							}
							Property prop2 = vh.getProperty(cl);
							if(prop2 != null) {
								if(!ph.node.isValueSet()) 
									ph.node.init(new EmptyParams(), ic);
								prop2.assignTo(ph.node, ph.propName);
							}
							
						} catch (ModelException e) {
							logAndThrow(e, st.name);
						}
					}
					break;
				}
				case INIT: {
					PropertyHandle ph = parseName(node, st.name);
					if(ph != null) {
						try {
							Property prop2 = ph.node.getProperty(ph.propName);
							if(!prop2.isValueSet())
								prop2.init(new EmptyParams(), ic);
							st.pc.initialize(new InstanceContext(ic, prop2));
						} catch (ModelException e) {
							logAndThrow(e, st.name);
						}
					}
				}
				}
			}
		}
	}

	@Override
	protected taiga.parser.node.Node getASTNode() {
		return astNode;
	}

	@Override
	public void init() throws TaigaException {
		for(Statement st: statements)
			st.pc.init();
		if(valuesParserContext != null)
			valuesParserContext.init();
	}
	
	private static class Statement {
		Op op;
		AName name;
		ParserContext pc;
		
		private Statement(Op op, AName name, ParserContext pc) {
			super();
			this.op = op;
			this.name = name;
			this.pc = pc;
		}
	}
	
	private enum Op {INIT, ASSIGN}

	@Override
	public boolean hasDependency(String typeName) {
		for(Statement st: statements)
			if(st.pc.hasDependency(typeName))
				return true;
		return false;
	};

	/** Parses property path */
	protected PropertyHandle parseName(Node root, AName name) throws TaigaException {
		PropertyHandle ph = new PropertyHandle(root, null);
		for(PIdent id: name.getParts()) {
			try {
				if(ph.propName != null) {
					Property prop = ph.node.getProperty(ph.propName);
					if(prop == null)
						throw new ModelException(ErrorCode.PROPERTY_NOT_FOUND, ph.propName);	
					if(!(prop instanceof Node))
						throw new ModelException(ErrorCode.INCOMPLETE_PATH, id.toString().trim(), ph.propName);	
					ph = new PropertyHandle((Node)prop, null);
				}
				Object propName = identToString(ph.node, id, ph.node.getPropertyNameClass()); 
				if(propName == ValueHolder.ERROR)
					return null;
				ph = new PropertyHandle(ph.node, propName);
			} catch (ModelException e) {
				logAndThrow(e, id);
				return null;
			}
		}
		return ph;
		
	}

	public boolean supportsMode(Mode mode) {
		switch(mode) {
		case VALUE: return valuesParserContext != null;
		case MAP: return valuesParserContext == null;
		default: return true;
		}
	}

	public Property createNodeForObject(Object obj) throws TaigaException {
		if(valuesParserContext != null)
			return valuesParserContext.createNodeForObject(obj);
		else {
			logAndThrow(ErrorCode.VALUE_NOT_DEFINED);
			return null;
		}
	}

}

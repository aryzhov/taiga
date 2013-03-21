package taiga.interfaces;




public class ErrorCode {

//	public static final ErrorCode INCLUDE_FILE_NOT_FOUND = new ErrorCode(1, "Include file not found: {0}");
//	public static final ErrorCode MISSING_TEMPLATE_NAME = new ErrorCode(2, "Missing template nameSrc");
//	public static final ErrorCode CYCLE_DETECTED = new ErrorCode(3, "Cycle detected: {0}");
	public static final ErrorCode PROPERTY_GET_FAILED = new ErrorCode(4, "Cannot get property value: {0}");
	public static final ErrorCode PROPERTY_SET_FAILED = new ErrorCode(5, "Cannot set property value: {0}");
	public static final ErrorCode PROPERTY_NOT_FOUND = new ErrorCode(6, "Property not found: {0}");
	public static final ErrorCode INCOMPATIBLE_TYPES = new ErrorCode(7, "Incompatible types: {0}, {1}");
	public static final ErrorCode CLASS_NOT_FOUND = new ErrorCode(8, "Class not found: {0}");
	public static final ErrorCode TYPE_NOT_FOUND = new ErrorCode(9, "Type not found: {0}");
//	public static final ErrorCode MISSING_PROPERTY_NAME = new ErrorCode(10, "Missing property name");
	public static final ErrorCode NEW_INSTANCE_ERROR = new ErrorCode(11, "Error while instantiating class: {0}");
//	public static final ErrorCode CLASS_MAPPING_REQUIRED = new ErrorCode(12, "Class mapping required for: {0}");
	public static final ErrorCode BEAN_INTROSPECTION_ERROR = new ErrorCode(13, "Bean introspection error: {0}");
//	public static final ErrorCode VALUE_PARSE_ERROR = new ErrorCode(14, "Value parse error. {0}, {1}");
//	public static final ErrorCode PRIMITIVE_TYPE_NO_HANDLER = new ErrorCode(15, "No suitable handler for primitive type {0}");
	public static final ErrorCode INVALID_LIST_INDEX = new ErrorCode(18, "Invalid list index: {0}, size: {1}");
	public static final ErrorCode LIST_SET_UNSUPPORTED = new ErrorCode(19, "Collection doesn't support set method");
	public static final ErrorCode PROP_SET_NOT_SUPPORTED = new ErrorCode(20, "Property {0} doesn't support set method");
	public static final ErrorCode MODEL_CLASS_INCOMPATIBLE = new ErrorCode(21, "Class {0} does not implement taiga.interfaces.Model");
	public static final ErrorCode MODEL_CLASS_NOT_FOUND = new ErrorCode(22, "Model class not found: {0}");
	public static final ErrorCode MODEL_CLASS_INSTANTIATION_ERROR = new ErrorCode(23, "Model class cannot be instantiated: {0}");
	public static final ErrorCode IO_EXCEPTION = new ErrorCode(25, "I/O Error occurred while reading {0}: {1}");
//	public static final ErrorCode OPERATOR_REQUIRED = new ErrorCode(26, "Operator required");
	public static final ErrorCode VALUE_NOT_SET = new ErrorCode(27, "Property value not set: {0}");
//	public static final ErrorCode PARAMETER_NOT_FOUND = new ErrorCode(28, "Required paramerer not specified: {0}");
//	public static final ErrorCode DUPLICATE_PARAMETER_NAME = new ErrorCode(29, "Duplicate parameter: {0}");
//	public static final ErrorCode MISSING_CLOSING_BRACE = new ErrorCode(30, "Missing closing brace");
//	public static final ErrorCode PARAM_NAME_AS_NUMBER = new ErrorCode(31, "Parameter nameSrc cannot be a number: {0}");
//	public static final ErrorCode ATTRIBUTE_NOT_DEFINED = new ErrorCode(32, "Attribute not defined: {0}");
//	public static final ErrorCode INVALID_VARARGS_INDEX = new ErrorCode(33, "Invalid varargs index: {0}");
	public static final ErrorCode CLASS_RESOURCE_NOT_FOUND = new ErrorCode(35, "Class resource not found: class {0}, resource {1}");
	public static final ErrorCode CANNOT_INSTANTIATE_ABSTRACT_CLASS = new ErrorCode(36, "Cannot instantiate abstract class: {0}");
	public static final ErrorCode FILE_NOT_FOUND = new ErrorCode(37, "File not found: {0}");
	public static final ErrorCode NULL_VALUE_NOT_ALLOWED = new ErrorCode(38, "Null value not allowed");
	public static final ErrorCode INVALID_PROPERTY_NAME_CLASS = new ErrorCode(39, "Property class incompatible: {0}, required {1}");
	public static final ErrorCode INCOMPLETE_PATH = new ErrorCode(40, "Can't reach property {0} because property {1} is not a node");
//	public static final ErrorCode TEMPLATE_NOT_A_NODE = new ErrorCode(41, "Template {1} is not a node, can't have statements");
	public static final ErrorCode ARRAY_IS_EMPTY = new ErrorCode(42, "Cannot reference the last element of an empty array");
//	public static final ErrorCode PROPERTY_DOES_NOT_SUPPORT_ADD = new ErrorCode(43, "Property doesn't support add operation: {0}");
//	public static final ErrorCode ENUM_VALUE_NOT_FOUND = new ErrorCode(44, "Enum value not found: {0}");
	public static final ErrorCode PROPERTY_READ_ONLY = new ErrorCode(45, "Property is read-only: {0}");
	public static final ErrorCode RESOURCE_NOT_FOUND = new ErrorCode(46, "Resource not found: {0}");
	public static final ErrorCode SYNTAX_ERROR = new ErrorCode(47, "Syntax error: {0}");
	public static final ErrorCode INVALID_NUMBER = new ErrorCode(48, "Invalid number");
	public static final ErrorCode NOT_IMPLEMENTED = new ErrorCode(49, "Not implemented");
	public static final ErrorCode NOT_OBJECT = new ErrorCode(50, "Cannot initialize type {0} as object");
	public static final ErrorCode NO_ROOT_VALUE = new ErrorCode(51, "Root value not specified");
	public static final ErrorCode NO_SOURCE_LOADED = new ErrorCode(52, "No source loaded");
	public static final ErrorCode NOT_ARRAY = new ErrorCode(53, "Type {0} as not an array");
	public static final ErrorCode NOT_NODE = new ErrorCode(54, "Cannot access property {0} because the parent is not a node");
	public static final ErrorCode DUPLICATE_TYPE = new ErrorCode(55, "Type {0} is already defined");
	public static final ErrorCode TYPE_DEF_NOT_SUPPORTED = new ErrorCode(56, "Type definition cannot appear here");
	public static final ErrorCode NOT_SUPPORTED = new ErrorCode(57, "Not supported");
//	public static final ErrorCode PARAMETER_VALUE_TYPE_MISMATCH = new ErrorCode(58, "Parameter value type mismatch. Parameter type is {0}, value type is {1}");
//	public static final ErrorCode MISSING_PARAMETER = new ErrorCode(59, "Missing parameter value: {0}");
//	public static final ErrorCode NAMED_AND_UNNAMED_PARAM_MIX = new ErrorCode(60, "Mixing named and unnamed parameters is not allowed");
	public static final ErrorCode UNRESOLVED = new ErrorCode(61, "Unresolved parameter {0} for object {1}" );
	public static final ErrorCode MISSING_PARAM = new ErrorCode(62, "Parameter {0} not specified for object {1}");
	public static final ErrorCode TOO_MANY_PARAMS = new ErrorCode(63, "Too many parameters for {0}");
	public static final ErrorCode INVALID_PARAM_INDEX = new ErrorCode(64, "Invalid parameter index: {0}");
	public static final ErrorCode SIGNATURE_MISMATCH = new ErrorCode(65, "Type signature mismatch. Required: {0}");
	public static final ErrorCode OBJECT_PARAM_ERROR = new ErrorCode(66, "Error while computing parameter {0}");
	public static final ErrorCode PROPERTY_ALREADY_ASSIGNED = new ErrorCode(67, "Property already assigned: {0}");
	public static final ErrorCode INVALID_TYPE_NAME_CLASS = new ErrorCode(68, "Invalid type name class {0}");
	public static final ErrorCode CANNOT_PARSE = new ErrorCode(69, "Cannot parse value {0} for class {1}");
	public static final ErrorCode CANNOT_CONVERT = new ErrorCode(70, "Cannot convert value {0} from type {1} to type {2}");
	public static final ErrorCode INVALID_BOOLEAN = new ErrorCode(71, "Invalid boolean value: {0}");
	public static final ErrorCode CIRCULAR_DEPENDENCY = new ErrorCode(73, "Circular dependency: {0}");
	public static final ErrorCode CANNOT_INSTANTIATE = new ErrorCode(74, "Don't know how to instantiate class: {0}");
	public static final ErrorCode SELECTOR_NOT_OBJECT = new ErrorCode(75, "Selector must be an object or an array");
	public static final ErrorCode INVALID_ENUM_VALUE = new ErrorCode(76, "Invalid enum value. Class {0}. value {1}");
	public static final ErrorCode INCOMPATIBLE_ATTR_TYPE = new ErrorCode(77, "Attribute value {0} is incompatible with class {1}");
	public static final ErrorCode CANNOT_RESOLVE_NAME = new ErrorCode(78, "Cannot resolve name: {0}");
	public static final ErrorCode PARAM_SIGNATURE_MISMATCH = new ErrorCode(79, "Incorrect number of parameters or parameter type(s)");
	public static final ErrorCode NO_DEFAULT_MODEL = new ErrorCode(80, "Default model must be specified in order to initialize objects");
	public static final ErrorCode CANNOT_INSTANTIATE_INNER = new ErrorCode(81, "Can't find enclosing instance for inner class: {0}");
	public static final ErrorCode CANNOT_MIX_ASSIGNMENTS_AND_VALUES = new ErrorCode(82, "Cannot mix assignments and values");
	public static final ErrorCode VALUE_NOT_DEFINED = new ErrorCode(83, "This taiga file does not define any values");
	public static final ErrorCode SINGLE_VALUE_EXPECTED = new ErrorCode(84, "This file defined an array of values");
	public static final ErrorCode CANNOT_INITIALIZE_NULL = new ErrorCode(85, "Cannot initialize a null object");
	public static final ErrorCode CANNOT_FIND_ENCLOSING_INSTANCE = new ErrorCode(86, "Cannot find enclosing instance of class {0}");
	public static final ErrorCode NOT_NUMBER = new ErrorCode(87, "Not a number");
	public static final ErrorCode LOOP_OUTSIDE_ARRAY = new ErrorCode(88, "Loop can be used only inside an array");
	public static final ErrorCode ASSERTION_ERROR = new ErrorCode(89, "An internal error has occurred");
	
	private int code;
	private String message;
	
	public ErrorCode(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ErrorCode))
			return false;
		ErrorCode ec = (ErrorCode) obj;
		return ec.code == code;
	}
	
	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public String toString() {
		return Integer.toString(code);
	}
	
	public String format(Object[] parameters) {
		String s = message;
		for(int i = 0; i < parameters.length; i++) {
			Object obj = parameters[i];
			if(obj instanceof Class)
				obj = ((Class)obj).getName().replaceAll("\\$", ".");
			String p = obj == null ? "null" : "'"+obj.toString()+"'";
			s = s.replaceAll("\\{"+i+"\\}", p);
		}
		return s + " (error #"+code+")";
	}
}

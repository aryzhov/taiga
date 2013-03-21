package taiga.interfaces;

public class ParamDef {
	public String name;
	public TypeInstance type;
	public Object defaultValue;
	public boolean isDefaultValueSet;
	
	public ParamDef(String name, TypeInstance type, Object defaultValue,
			boolean isDefaultValueSet) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.isDefaultValueSet = isDefaultValueSet;
	}
	
}

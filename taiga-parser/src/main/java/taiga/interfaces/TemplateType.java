package taiga.interfaces;


public class TemplateType {
	public final String name;
	
	public TemplateType(String name) {
		this.name = name;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(name);
	}
	
	@Override
	public String toString() {
		return name;
	}
}

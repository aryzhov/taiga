package taiga.test;

public class Config {
	private String name;
	private int count;
	private ValueSampler values = new ValueSampler();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public ValueSampler getValues() {
		return values;
	}

	public void setValues(ValueSampler values) {
		this.values = values;
	}
	
}

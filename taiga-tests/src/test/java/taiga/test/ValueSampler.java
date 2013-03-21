package taiga.test;

import java.util.Date;

public class ValueSampler {
	public String stringValue;
	public int intValue;
	public Date dateValue;
	public Object objectValue;
	public Nested nested;
	public Nested nested2;
	public boolean booleanValue;
	
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}
	public Object getObjectValue() {
		return objectValue;
	}
	public void setObjectValue(Object objectValue) {
		this.objectValue = objectValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}
	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	public Nested getNested() {
		return nested;
	}
	public void setNested(Nested nested) {
		this.nested = nested;
	}

	public Nested getNested2() {
		return nested2;
	}
	public void setNested2(Nested nested2) {
		this.nested2 = nested2;
	}

	public class Nested {
		
		public String stringValue;

		public Nested() {
		}

		public String getStringValue() {
			return stringValue;
		}

		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}
	}
	
}

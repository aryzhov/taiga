package taiga.html;

import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLHeadElement;

public enum Attr {
	
	id(String.class),
	cl(String.class), 
	title(String.class),
	head(HTMLHeadElement.class),
	body(HTMLBodyElement.class), 
	text(String.class), 
	href(String.class), 
	type(String.class),
	rel(String.class), 
	lang(String.class),
	src(String.class), 
	tabindex(Integer.class),
	style(String.class), 
	margin(String.class), 
	accesskey(String.class),
	disabled(String.class),
	name(String.class),
	value(String.class), 
	target(String.class);
	
	public Class<?> baseClass;
	
	private Attr(Class<?> baseClass) {
		this.baseClass = baseClass;
	}

	public Class<?> getBaseClass() {
		return baseClass;
	}
	
	@Override
	public String toString() {
		return this == cl ? "class" : super.toString();
	}
}

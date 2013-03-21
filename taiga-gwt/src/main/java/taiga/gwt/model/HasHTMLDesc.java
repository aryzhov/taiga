package taiga.gwt.model;

import taiga.interfaces.ModelException;

import com.google.gwt.user.client.ui.HasHTML;

class HasHTMLDesc extends InterfaceDesc<HasHTML> {
	
	public HasHTMLDesc(HasTextDesc hasText) {
		addInterface(hasText);
		add(new PropDesc<HasHTML, String>() {
			@Override
			public String getPropName() {
				return "html";
			}
			
			@Override
			public Class<?> getBaseClass() {
				return String.class;
			}
			
			@Override
			public String get(HasHTML widget) throws ModelException {
				return widget.getHTML();
			}
			
			@Override
			public void set(HasHTML widget, String value) throws ModelException {
				widget.setHTML(value);
			}
		});
	}
}

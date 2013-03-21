package taiga.gwt.model;

import taiga.interfaces.ModelException;
import taiga.interfaces.Params;

import com.google.gwt.user.client.ui.HTML;

class HTMLDesc extends WidgetDesc<HTML> {

	public HTMLDesc(HasHTMLDesc hasHTML) {
		addInterface(hasHTML);
	}

	@Override
	public String getTypeName() {
		return "HTML";
	}
	
	@Override
	public Class<?> getWidgetClass() {
		return HTML.class;
	}
	
	@Override
	public HTML newInstance(Params params) throws ModelException {
		return new HTML();
	}
}

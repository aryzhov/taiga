package taiga.gwt.model;

import com.google.gwt.user.client.ui.IsWidget;

import taiga.interfaces.ModelException;
import taiga.interfaces.Params;

public class PageDesc extends ClassDesc<Page> {
	public PageDesc() {
		add(new StringPropDesc<Page>() {

			@Override
			public String getPropName() {
				return "windowTitle";
			}

			@Override
			public String get(Page widget) throws ModelException {
				return widget.windowTitle;
			}

			@Override
			public void set(Page widget, String value) throws ModelException {
				widget.windowTitle = value;
			}
			
		});
		add(new IsWidgetPropDesc<Page>() {

			@Override
			public String getPropName() {
				return "root";
			}

			@Override
			public IsWidget get(Page widget) throws ModelException {
				return widget.root;
			}

			@Override
			public void set(Page widget, IsWidget value) throws ModelException {
				widget.root = value;
			}
			
		});
	}
	
	@Override
	public String getTypeName() {
		return "Page";
	}

	@Override
	public Class<?> getWidgetClass() {
		return Page.class;
	}
	
	@Override
	public Page newInstance(Params params) throws ModelException {
		return new Page();
	}
}

package taiga.gwt.model;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class Page implements IsWidget {
	public String windowTitle;
	public IsWidget root;
	
	@Override
	public Widget asWidget() {
		return root.asWidget();
	}
}

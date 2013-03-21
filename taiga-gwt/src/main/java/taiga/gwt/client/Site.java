package taiga.gwt.client;

import taiga.gwt.model.GWTModelFactory;
import taiga.gwt.model.Page;
import taiga.gwt.model.WidgetModel;
import taiga.interfaces.ErrorLog;
import taiga.interfaces.ErrorMessage;
import taiga.interfaces.TaigaException;
import taiga.parser.TaigaEngine;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Site implements EntryPoint {
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
	  final ErrorLog el = new ErrorLog(false);
	  new SiteLoader(el, "", "site.taiga") {
		@Override
		public void onLoad() {
			TaigaEngine te = new TaigaEngine();
			te.setModelFactory(new GWTModelFactory(te.getModelFactory()));
			te.setErrorLog(el);
			try {
				te.read(new SiteResourceProvider(this, "site.taiga"));
				Page page = te.instantiate(Page.class, null, "Page");
				if(page != null) {
					Window.setTitle(page.windowTitle);
					RootPanel.get().add(page.root);
				}
			} catch (TaigaException e) {
				
			} finally {
				if(!el.isEmpty()) {
					RootPanel.get().add(new HTML("Errors"));
					GWT.log("--- Errors --- ");
					for(ErrorMessage em: el.getMessages())
						GWT.log(em.toString());
				}
			}
		}
	  };
	  
  }

}

package taiga.gwt.model;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;

import com.google.gwt.user.client.ui.UIObject;

abstract class UIObjectDesc<W extends UIObject> extends ClassDesc<W> {
	
	public UIObjectDesc() {
		add(new StringPropDesc<UIObject>() {

			@Override
			public String getPropName() {
				return "styles";
			}

			@Override
			public String get(UIObject widget) throws ModelException {
				return widget.getStyleName();
			}

			@Override
			public void set(UIObject widget, String value)
					throws ModelException {
				widget.setStyleName(value);
			}
			
		});
		add(new BooleanPropDesc<UIObject>() {

			@Override
			public String getPropName() {
				return "visible";
			}

			@Override
			public Boolean get(UIObject widget) throws ModelException {
				return widget.isVisible();
			}

			@Override
			public void set(UIObject widget, Boolean value)
					throws ModelException {
				if(value == null)
					throw new ModelException(ErrorCode.NULL_VALUE_NOT_ALLOWED);
				widget.setVisible(value);
			}
		});
		
	}
}

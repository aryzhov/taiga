package taiga.gwt.model;

import taiga.interfaces.ModelException;

import com.google.gwt.user.client.ui.HasText;

class HasTextDesc extends InterfaceDesc<HasText> {

	public HasTextDesc() {
		add(new StringPropDesc<HasText>() {

			@Override
			public String getPropName() {
				return "src";
			}

			@Override
			public String get(HasText widget) throws ModelException {
				return widget.getText();
			}

			@Override
			public void set(HasText widget, String value) throws ModelException {
				widget.setText(value);
			}
		});
	}
}

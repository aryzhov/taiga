package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLHtmlElement;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Params;
import taiga.model.AbstractModel;

public class DocumentNode extends ElementNode<HTMLDocument> {
	
	private static final Set<Attr> ATTRS = set(Attr.title, Attr.head, Attr.body, Attr.lang);

	public DocumentNode(AbstractModel model) {
		super(model, HTMLDocument.class);
	}

	@Override
	protected HTMLDocument createInitValue(Params params, Iterable<taiga.interfaces.Node> scope) throws ModelException {
		return getDocument();
	}

	@Override
	public Set<Attr> getAttributes() {
		return ATTRS;
	}

	@Override
	protected Object getAttrValue(Attr attr) throws ModelException {
		switch(attr) {
		case title: return value.getTitle();
		case head: return (HTMLHeadElement) (((HTMLHtmlElement) value.getFirstChild()).getFirstChild());
		case body: return value.getBody();
		case lang: return ((HTMLElement)value.getFirstChild()).getLang();
		default: return super.getAttrValue(attr);
		}
	}

	@Override
	protected boolean setAttrValue(Attr attr, Object obj) throws ModelException {
		switch(attr) {
		case title: value.setTitle((String)obj); return true;
		case head:
		case body:throw new ModelException(null, this, attr, ErrorCode.PROPERTY_READ_ONLY, attr);
		case lang: ((HTMLElement)value.getFirstChild()).setLang((String) obj);
		default: return super.setAttrValue(attr, obj);
		}
	}
	
}

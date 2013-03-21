package taiga.test.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.w3c.dom.html.HTMLDocument;

import taiga.ClassResourceProvider;
import taiga.beans.BeansModelFactory;
import taiga.html.HTMLModelFactory;
import taiga.model.CoreModelFactory;
import taiga.parser.TaigaEngine;

public class TestHTMLModel extends TestCase {
	private static final ArrayList<String> NO_ERRORS = new ArrayList<String>(0);

	private TaigaEngine taiga;

	@Override
	protected void setUp() throws Exception {
		taiga = new TaigaEngine();
		taiga.setModelFactory(new HTMLModelFactory(new BeansModelFactory(new CoreModelFactory(null))));
		taiga.read(new ClassResourceProvider(getClass(), "TestHTMLModel.taiga"));
	}
	
	public void test1() throws Exception {
		HTMLDocument doc = taiga.instantiate(HTMLDocument.class, null, "Test1");
		assertEquals(NO_ERRORS, taiga.getErrorLog().getMessages());
		assertEquals("Hello", doc.getTitle());
		assertEquals("Taiga rulez!", doc.getBody().getFirstChild().getTextContent());
	}
}

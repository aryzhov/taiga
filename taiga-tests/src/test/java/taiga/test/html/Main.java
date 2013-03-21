package taiga.test.html;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.html.HTMLDocument;

import taiga.ClassResourceProvider;
import taiga.beans.BeansModelFactory;
import taiga.html.HTMLModelFactory;
import taiga.interfaces.ErrorMessage;
import taiga.model.CoreModelFactory;
import taiga.parser.TaigaEngine;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		TaigaEngine taiga = new TaigaEngine();
		taiga.setModelFactory(new HTMLModelFactory(new BeansModelFactory(new CoreModelFactory(null))));
		taiga.read(new ClassResourceProvider(Main.class, "page.taiga"));
		Map<String, HTMLDocument> pages = (Map<String, HTMLDocument>)taiga.instantiate(Map.class);
		for(ErrorMessage em: taiga.getErrorLog().getMessages())
			System.err.println(em.toString());
		for(HTMLDocument doc: pages.values())
			writeDoc(doc);
	}

	private static void writeDoc(HTMLDocument doc) throws TransformerFactoryConfigurationError, TransformerException, UnsupportedEncodingException {
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.METHOD, "html");
		t.setOutputProperty(OutputKeys.VERSION, "4.01");
		t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
		                    "-//W3C//DTD HTML 4.01//EN");
		t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
		               "http://www.w3.org/TR/html4/strict.dtd");
		StreamResult sr = new StreamResult(System.out);
		t.transform(new DOMSource(doc), sr);
	}


}

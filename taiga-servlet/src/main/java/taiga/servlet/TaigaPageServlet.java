package taiga.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import taiga.html.HTMLModel;
import taiga.html.HTMLModelFactory;
import taiga.interfaces.ErrorLog;
import taiga.interfaces.ErrorMessage;
import taiga.interfaces.PathUtils;
import taiga.interfaces.StringUtils;
import taiga.interfaces.TaigaException;
import taiga.parser.TaigaEngine;

public class TaigaPageServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String basePath = getServletConfig().getInitParameter("path");
		if(basePath == null)
			basePath = "";
		String fileNames = getServletConfig().getInitParameter("files");
		if(fileNames == null)
			throw new ServletException("Specify file init-param");
		ErrorLog el = new ErrorLog(false);
		TaigaEngine tp = new TaigaEngine();
		tp.setErrorLog(el);
		tp.setModelFactory(new HTMLModelFactory(tp.getModelFactory()));
		try {
			for(String fn: fileNames.split(",")) {
				tp.read(new ServletResourceProvider(getServletContext(), basePath, fn));
			}
			String pageName = PathUtils.removeLeadingSlash(PathUtils.cleanUp(PathUtils.concatPath(req.getServletPath(), req.getPathInfo()), true));
			if(pageName == null)
				pageName = "";
			pageName = PathUtils.removeExtension(pageName, getServletConfig().getInitParameter("ext"));
			Map<String, Object> attrs = new HashMap<String, Object>();
			attrs.put("pageName", pageName);
			Node doc = tp.instantiate(Node.class, attrs, "ThePage");
			if(doc != null) {
				resp.setStatus(HttpServletResponse.SC_OK);
				writeDoc(doc, resp.getWriter());
			} else {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (TaigaException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		if(!el.isEmpty()) {
			log("Errors:");
			for(ErrorMessage em: el.getMessages())
				log(em.toString());
		}
	}
	
	private void writeDoc(Node doc, Writer w) throws ServletException, IOException {
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			boolean html5 = StringUtils.equals(getServletConfig().getInitParameter("version"), "5", true);
			if(html5) {
				t.setOutputProperty(OutputKeys.METHOD, "html");
				t.setOutputProperty(OutputKeys.VERSION, "5.0");
				w.append("<!DOCTYPE HTML>\n");
			} else {
				t.setOutputProperty(OutputKeys.METHOD, "html");
				t.setOutputProperty(OutputKeys.VERSION, "4.01");
				t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
				                    "-//W3C//DTD HTML 4.01//EN");
				t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
				               "http://www.w3.org/TR/html4/strict.dtd");
			}
			StreamResult sr = new StreamResult(w);
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(doc), sr);
		} catch (TransformerFactoryConfigurationError ex) {
			throw new ServletException(ex);
		} catch (TransformerException ex) {
			throw new ServletException(ex);
		}
	}
	
	public void logError(String msg, Throwable ex) {
		System.err.println(msg);
		if(ex != null)
			ex.printStackTrace();
	}
	
}

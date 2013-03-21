package taiga.servlet;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;

import taiga.IOUtils;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.PathUtils;
import taiga.interfaces.ResourceProvider;

public class ServletResourceProvider implements ResourceProvider {

	private ServletContext sc;
	private String basePath;
	private String name;
	private String absoluteName;

	public ServletResourceProvider(ServletContext sc, String basePath, String name) {
		super();
		this.sc = sc;
		if(basePath == null)
			basePath = "/"; 
		if(!basePath.startsWith("/"))
			basePath = "/" + basePath;
		if(!basePath.endsWith("/"))
			basePath = basePath + "/";
		this.basePath = basePath;
		this.name = PathUtils.normalizePath(basePath, name);
		this.absoluteName = PathUtils.concatPath(basePath, name);
	}
	
	public String getRelativeName() {
		return name;
	}

	public String getAbsoluteName() {
		return absoluteName;
	}

	public String read() throws ModelException {
		InputStream is = sc.getResourceAsStream(absoluteName);
		if(is == null)
			throw new ModelException(ErrorCode.RESOURCE_NOT_FOUND, absoluteName);
		else {
			return IOUtils.readAll(new InputStreamReader(is));
		}
	}

	public ResourceProvider getResource(String name2) {
		return new ServletResourceProvider(sc, basePath, PathUtils.concatPath(PathUtils.getPath(name), name2));
	}

}


package taiga;

import java.io.InputStream;
import java.io.InputStreamReader;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.PathUtils;
import taiga.interfaces.ResourceProvider;

public class ClassResourceProvider implements ResourceProvider {
	
	private Class<?> base;
	private String name;
	private String absoluteName;
	
	public ClassResourceProvider(Class<?> base, String name) {
		super();
		this.base = base;
		String basePath = getClassPath(base); 
		this.name = PathUtils.normalizePath(basePath, name);
		this.absoluteName = PathUtils.concatPath(basePath, name);
	}

	private static String getClassPath(Class<?> cl) {
		String path = cl.getPackage().getName();
		return PathUtils.cleanUp("/" + path.replaceAll("\\.", "/"), false); 
	}
	
	@Override
	public String getAbsoluteName() {
		return absoluteName;
	}

	@Override
	public ResourceProvider getResource(String name2) {
		return new ClassResourceProvider(base, PathUtils.concatPath(PathUtils.getPath(name), name2));
	}

	@Override
	public String read() throws ModelException {
		InputStream is = base.getResourceAsStream(name);
		if(is == null)
			throw new ModelException(ErrorCode.CLASS_RESOURCE_NOT_FOUND, base, name);
		else {
			return IOUtils.readAll(new InputStreamReader(is));
		}
	}

	@Override
	public String getRelativeName() {
		return name;
	}

}

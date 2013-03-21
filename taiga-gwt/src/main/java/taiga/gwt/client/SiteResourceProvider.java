package taiga.gwt.client;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.PathUtils;
import taiga.interfaces.ResourceProvider;

public class SiteResourceProvider implements ResourceProvider {

	private String name;
	private SiteLoader loader;
	
	public SiteResourceProvider(SiteLoader loader, String name) {
		this.loader = loader;
		this.name = PathUtils.normalizePath(loader.getBasePath(), name);
	}

	@Override
	public String getAbsoluteName() {
		return PathUtils.concatPath(loader.getBasePath(), name);
	}

	@Override
	public ResourceProvider getResource(String name2) {
		return new SiteResourceProvider(loader, name2);
	}

	@Override
	public String read() throws ModelException {
		String result = loader.getContent(name);
		if(result == null)
			throw new ModelException(ErrorCode.FILE_NOT_FOUND, getAbsoluteName());
		else
			return result;
	}

	@Override
	public String getRelativeName() {
		return name;
	}


}

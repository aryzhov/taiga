package taiga.interfaces;

public class PathUtils {

	public static String concatPath(String...paths) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < paths.length; i++) {
			String p = paths[i];
			if(StringUtils.isEmpty(p))
				continue;
			p = cleanUp(p, i == paths.length-1);
			if(isAbsolute(p))
				result = new StringBuilder();
//			if(result.length() > 0 && p.startsWith("./"))
			result.append(p);
		}
		return result.toString();
	}

	public static boolean isAbsolute(String path) {
		return path.startsWith("/");
	}
	
	public static String normalizePath(String basePath, String name) {
		basePath = cleanUp(basePath, false);
		name = cleanUp(name, true);
		if(name.startsWith(basePath))
			name = name.substring(basePath.length());
		return name;
	}

	public static String getPath(String path) {
		path = cleanUp(path, true);
		int k = path.lastIndexOf("/");
		return k < 0 ? "" : path.substring(0, k+1);
	}
	
	public static String cleanUp(String path, boolean name) {
		if(path == null)
			return null;
		path = path.replaceAll("\\\\", "/").replaceAll("/{2,}", "/").replaceAll("\\s+/", "/").replaceAll("/\\s+", "/").trim();
		if(path.startsWith("./"))
			path = path.substring(2);
		boolean trailingSlash = path.length() > 0 && path.endsWith("/");
		if(name && trailingSlash) {
			path = path.substring(0, path.length()-1);
		} else if(!name && !trailingSlash && !path.isEmpty())
			path += "/";
		return path;
	}
	
	public static String removeLeadingSlash(String path) {
		if(path != null && path.startsWith("/"))
			return path.substring(1);
		else
			return path;
	}

	public static boolean isValid(String path, boolean name) {
		if(name)
			return !StringUtils.isEmpty(path) && !path.endsWith("/");
		else
		return StringUtils.isEmpty(path) || path.endsWith("/");
	}

	public static String removeExtension(String path, String ext) {
		if(ext == null || path == null)
			return path;
		if(path.endsWith(ext))
			return path.substring(0, path.length()-ext.length());
		else
			return path;
	}
	
}

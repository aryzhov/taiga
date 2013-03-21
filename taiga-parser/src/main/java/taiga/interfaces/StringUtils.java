package taiga.interfaces;

public class StringUtils {

	public static boolean equals(String a, String b, boolean ignoreCase) {
		if(a == null || b == null)
			return a == b;
		else
			return ignoreCase ? a.equalsIgnoreCase(b) : a.equals(b);
	}

	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static String toString(Object o) {
		return o == null ? "null" : o.toString();
	}
}

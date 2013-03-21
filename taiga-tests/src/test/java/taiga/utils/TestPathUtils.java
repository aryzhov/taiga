package taiga.utils;

import taiga.interfaces.PathUtils;
import junit.framework.TestCase;

public class TestPathUtils extends TestCase {
	
	public void testCleanUp() {
		assertEquals(null, PathUtils.cleanUp(null, false));
		assertEquals("", PathUtils.cleanUp("", false));
		assertEquals("", PathUtils.cleanUp("", true));
		assertEquals("/", PathUtils.cleanUp("/", false));
		assertEquals("/", PathUtils.cleanUp("//", false));
		assertEquals("abc/", PathUtils.cleanUp("./abc", false));
		assertEquals("abc", PathUtils.cleanUp("./abc", true));
		assertEquals("abc", PathUtils.cleanUp("abc", true));
		assertEquals("/abc/def", PathUtils.cleanUp("/abc /def", true));
		assertEquals("/abc/def/gh i/", PathUtils.cleanUp(" /abc //def\\gh i ", false));
	}

	public void testIsAbsolute() {
		assertTrue(PathUtils.isAbsolute("/abc/def"));
		assertFalse(PathUtils.isAbsolute("abc/def"));
	}
	
	public void testNormalizePath() {
		assertEquals("ghi", PathUtils.normalizePath("/abc/def", "/abc/def/ghi"));
	}

	public void testsGetPath() {
		assertEquals("/abc/def/", PathUtils.getPath("/abc/def/ghi"));
	}
	
	public void concatPath() {
		assertEquals("abc", PathUtils.concatPath("./abc"));
		assertEquals("/abc/def/ghi/xyz", PathUtils.concatPath("\\abc\\def ", "hgi/", "xyz"));
		assertEquals("/ghi/xyz", PathUtils.concatPath("/abc/def", "/hgi", "xyz"));
		assertEquals("abc/def/xyz", PathUtils.concatPath("abc/def", "./xyz"));
	}

}

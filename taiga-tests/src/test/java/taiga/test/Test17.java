package taiga.test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test17 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Set<String> attrNames = te.getAttributeNames();
		assertEquals(makeSet("b"), attrNames);
		assertEquals(makeMap("a", 13, "c", 13), te.instantiate(makeMap("b", 13)));
	}
}
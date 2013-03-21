package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test16 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Object value = te.instantiate(makeMap("x", 13, "y", 15));
		checkNoErrors();
		assertEquals(makeMap("a", 13, "b", "def"), value);
	}
}
package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test5 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Object value = te.instantiateMap();
		checkNoErrors();
		assertEquals(makeMap("a", makeMap("x", 1), 
				"b", makeMap("x", 1, "y", 2),
				"c", 13,
				"d", 13),
				value);
	}

}
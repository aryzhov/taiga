package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test13 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Object value = te.instantiate();
		checkNoErrors();
		assertEquals(makeList(
				makeMap("a", 1),
				makeMap("a", 2),
				makeMap("a", 3),
				makeMap("b", 4),
				makeMap("b", 5)
				), value);
	}
}
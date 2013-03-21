package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test10 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Object value = te.instantiate();
		checkNoErrors();
		assertEquals(makeList(
					makeMap("m", 1, "n", 10, "k", 10), 
					makeMap("m", 1, "n", 10, "k", 20),
					makeMap("m", 1, "n", 10, "k", 30)
					), value);
	}
}
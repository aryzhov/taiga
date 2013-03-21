package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test15 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Object value = te.instantiateList();
		checkNoErrors();
		assertEquals(makeList("a", "b", "c"), value);
	}
}
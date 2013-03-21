package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test4 extends TestBase {

	@Test
	public void test() throws TaigaException {
		String value = te.instantiate(String.class, null);
		checkNoErrors();
		assertEquals("Hello, \"world\"!", value);
	}
}

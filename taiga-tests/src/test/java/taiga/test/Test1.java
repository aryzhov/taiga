package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test1 extends TestBase {

	@Test
	public void test() throws TaigaException {
		String s = te.instantiate(String.class);
		checkNoErrors();
		assertEquals("Hello", s);
	}

}

package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test12 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Object value = te.instantiate();
		checkNoErrors();
		assertEquals(makeList("Hello"), value);
	}
}
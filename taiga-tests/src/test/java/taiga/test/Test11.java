package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test11 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Object value = te.instantiate();
		checkNoErrors();
		assertEquals(makeList(
					makeList(1, 1, 1), 
					makeList(2, 2, 2), 
					makeList(3, 3, 3) 
					), value);
	}
}
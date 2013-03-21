package taiga.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test19 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Collection list = te.instantiate(Collection.class);
		int x = 10, y = 2;
		assertEquals(makeList(x+y,x-y,x*y,x/y,-x, x * y / x, (x + y + x ) * y / x - y), list);
	}
}

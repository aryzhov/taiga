package taiga.test;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test20 extends TestBase {
	@Test
	public void test() throws TaigaException {
		Map map = te.instantiate(Map.class);
		double d = 3.14;
		assertEquals(map, makeMap("x", d /2));
	}
}

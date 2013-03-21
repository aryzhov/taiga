package taiga.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test21 extends TestBase {
	@Test
	public void test() throws TaigaException {
		Collection list = te.instantiateList();
		List<Integer> expected = new ArrayList<Integer>();
		for(int i = -3; i <= 3; i++)
			for(int j = -3; j <= 3; j++)
				expected.add(i * j);
		assertEquals(expected, list);
	}
}
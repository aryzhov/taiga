package taiga.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test3 extends TestBase {

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws TaigaException {
		List value = te.instantiate(List.class);
		checkNoErrors();
		assertEquals(Arrays.asList(1,"abc",false), value);
	}
}

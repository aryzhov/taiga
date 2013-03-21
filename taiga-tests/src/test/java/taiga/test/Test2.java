package taiga.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test2 extends TestBase {

	@Test
	public void test() throws TaigaException {
		Object value = te.instantiate();
		checkNoErrors();
		Map exp = makeMap(
				"hello", "World",
				"string", "abc def",
				"integer", 13,
				"double", -0.55,
				"boolean", true,
				"array", Arrays.asList(1,2,3),
				"object", makeMap("first_name", "Jon", "last_name", "Smith"),
				"empty", makeMap(),
				"nullValue", null
				);
		assertEquals(exp, value);
	}

}

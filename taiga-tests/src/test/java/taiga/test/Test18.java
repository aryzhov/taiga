package taiga.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import taiga.interfaces.TaigaException;

public class Test18 extends TestBase {

	@Test
	public void test() throws TaigaException {
			
		ValueSampler vs = te.instantiate(ValueSampler.class);
		assertEquals("Hello", vs.getNested().getStringValue());
		assertEquals("world", vs.getNested2().getStringValue());
	
		ValueSampler vs2 = new ValueSampler();
		te.initialize(vs2);
		assertEquals("Hello", vs2.getNested().getStringValue());
		assertEquals("world", vs2.getNested2().getStringValue());
		
	}
}
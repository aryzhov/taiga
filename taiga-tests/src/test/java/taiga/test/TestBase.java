package taiga.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;

import taiga.ClassResourceProvider;
import taiga.beans.BeansModelFactory;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.ErrorMessage;
import taiga.interfaces.TaigaException;
import taiga.model.CoreModelFactory;
import taiga.parser.TaigaEngine;

public abstract class TestBase {
	protected TaigaEngine te;

	@Before
	public void setUp() throws TaigaException {
		te = new TaigaEngine();
		te.setModelFactory(new CoreModelFactory(new BeansModelFactory(null)));
		te.read(new ClassResourceProvider(getClass(), getClass().getSimpleName() + ".taiga"));
	}
	
	protected void checkNoErrors() {
		assertEquals(Collections.EMPTY_LIST, te.getErrorLog().getMessages());
	}

	protected void checkError(ErrorCode code) {
		for(ErrorMessage em: te.getErrorLog().getMessages())
			if(em.errorCode.equals(code))
				return;
		Assert.fail();
	}

	@SuppressWarnings("unchecked")
	protected Map makeMap(Object... nameValues) {
		Map result = new HashMap();
		for(int i = 0; i < nameValues.length-1; i+= 2)
			result.put(nameValues[i], nameValues[i+1]);
		return result;
	}
	
	protected List makeList(Object... elements) {
		return Arrays.asList(elements);
	}

	protected Set makeSet(Object... objects) {
		return new HashSet(Arrays.asList(objects));
	}
}

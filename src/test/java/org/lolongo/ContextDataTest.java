package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.data.StringData;

public class ContextDataTest {

	private ContextData context;

	@Before
	public void initContext() {
		context = new ContextData();
	}

	@Test
	public void testPutGet() throws Exception {

		context.put(new StringData(new RefId<String>("ref1"), "Value_1"));
		final String result = context.get(new RefId<String>("ref1"));
		Assert.assertEquals("Value_1", result);
	}

	@Test
	public void testPutGetNoMismatch() throws RefAlreadyExists, RefNotFound {

		context.put(new StringData(new RefId<String>("ref1"), "Value_1"));
		context.put(new StringData(new RefId<String>("ref2"), "Value_2"));
		Assert.assertEquals("Value_1", context.get(new RefId<String>("ref1")));
		Assert.assertEquals("Value_2", context.get(new RefId<String>("ref2")));
	}

	@Test(expected = RefAlreadyExists.class)
	public void testRefUniqueInContext() throws RefAlreadyExists, RefNotFound {

		context.put(new StringData(new RefId<String>("ref1"), "Value_1"));
		try {
			context.put(new StringData(new RefId<String>("ref1"), "Value_2"));
		} catch (final RefAlreadyExists e) {
			Assert.assertEquals(new RefId<String>("ref1"), e.getRef());
			throw e;
		} finally {
			Assert.assertEquals("Value_1", context.get(new RefId<String>("ref1")));
		}
	}

}

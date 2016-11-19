package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContextRootTest {

	private ContextRoot contextRoot;

	@Before
	public void init() {
		contextRoot = new ContextRoot();
	}

	@Test
	public void testGetName() {
		Assert.assertNull(contextRoot.getName());
	}

	@Test
	public void testGetParent() {
		Assert.assertNull(contextRoot.getParent());
	}

	@Test
	public void testIsRoot() {
		Assert.assertTrue(contextRoot.isRoot());
	}

	@Test
	public void testGetRoot() {
		Assert.assertEquals(contextRoot, contextRoot.getRoot());
	}

	@Test
	public void testGetAbsoluteContext() {
		Assert.assertEquals("/", ContextRef.getAbsoluteContextRef(contextRoot));
	}
}

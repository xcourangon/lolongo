package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ContextNodeTest {

    private ContextNode parent;

    @Before
    public void initContext() {
	parent = new ContextNode("parent");
    }

    @Test(expected = RefNotFound.class)
    public void testDefaultParent() throws ContextException {
	final Context context = new ContextNode("context");
	try {
	    context.get(new RefId<String>("none"));
	} catch (final RefNotFound e) {
	    Assert.assertEquals(new RefId<>("none"), e.getRef());
	    Assert.assertEquals(context, e.getContext());
	    throw e;
	}
    }

    @Test
    public void testContextTree() throws Exception {
	final ContextNode subcontext = new ContextNode("subcontext");
	parent.addSubcontext(subcontext);
	final ContextNode subcontext2 = new ContextNode("subcontext2");
	parent.addSubcontext(subcontext2);

	Assert.assertEquals(parent, subcontext.getParent());
	Assert.assertEquals(parent, subcontext2.getParent());
	Assert.assertEquals(2, parent.getSubcontexts().size());
	Assert.assertTrue(parent.getSubcontexts().contains(subcontext));
	Assert.assertTrue(parent.getSubcontexts().contains(subcontext2));
    }

    @Test(expected = ContextAlreadyExists.class)
    public void testSubcontextUniqueByName() throws ContextAlreadyExists {
	parent.addSubcontext(new ContextNode("child"));
	try {
	    parent.addSubcontext(new ContextNode("child"));
	} catch (final ContextAlreadyExists e) {
	    Assert.assertEquals("child", e.getName());
	    throw e;
	}
    }

    @Test
    public void testGetSubcontext() throws Exception {
	final ContextNode subcontext = new ContextNode("subcontext");
	parent.addSubcontext(subcontext);
	final ContextNode subcontext2 = new ContextNode("subcontext2");
	parent.addSubcontext(subcontext2);

	Assert.assertEquals(subcontext, parent.getSubcontext("subcontext"));
	Assert.assertEquals(subcontext2, parent.getSubcontext("subcontext2"));
    }

    @Test
    public void testInheritance() throws Exception {
	parent.put(new RefId<String>("ref"), "val_from_parent");
	final ContextNode subcontext = new ContextNode("child");
	parent.addSubcontext(subcontext);

	Assert.assertEquals("val_from_parent", subcontext.get(new RefId<String>("ref")));
    }

    @Test(expected = RefNotFound.class)
    public void testInheritanceFalse() throws Exception {
	parent.put(new RefId<String>("ref"), "val_from_parent");
	final ContextNode subcontext = new ContextNode("child", false);
	parent.addSubcontext(subcontext);

	try {
	    subcontext.get(new RefId<String>("ref"));
	} catch (final RefNotFound e) {
	    Assert.assertEquals(new RefId<>("ref"), e.getRef());
	    Assert.assertEquals(subcontext, e.getContext());
	    throw e;
	}
    }

    @Test(expected = RefNotFound.class)
    public void testGetNotRecursive() throws Exception {
	parent.put(new RefId<String>("ref"), "val_from_parent");
	final ContextNode subcontext = new ContextNode("child");
	parent.addSubcontext(subcontext);

	try {
	    subcontext.get(new RefId<String>("ref"), false);
	} catch (final RefNotFound e) {
	    Assert.assertEquals(new RefId<>("ref"), e.getRef());
	    Assert.assertEquals(subcontext, e.getContext());
	    throw e;
	}
    }

    @Test
    public void testOverriding() throws Exception {
	parent.put(new RefId<String>("ref"), "val_from_parent");
	final ContextNode subcontext = new ContextNode("child");
	subcontext.put(new RefId<String>("ref"), "val_from_child");
	parent.addSubcontext(subcontext);

	Assert.assertEquals("val_from_child", subcontext.get(new RefId<String>("ref")));
    }

    @Test
    public void testIsParentOf() throws Exception {
	final ContextNode root = new ContextNode("root");
	final ContextNode subcontext1 = new ContextNode("subcontext1");
	root.addSubcontext(subcontext1);
	final ContextNode subsubcontext11 = new ContextNode("subsubcontext1");
	subcontext1.addSubcontext(subsubcontext11);
	final ContextNode subsubcontext12 = new ContextNode("subsubcontext2");
	subcontext1.addSubcontext(subsubcontext12);
	final ContextNode subcontext2 = new ContextNode("subcontext2");
	root.addSubcontext(subcontext2);
	final ContextNode subsubcontext21 = new ContextNode("subsubcontext1");
	subcontext2.addSubcontext(subsubcontext21);
	final ContextNode subsubcontext22 = new ContextNode("subsubcontext2");
	subcontext2.addSubcontext(subsubcontext22);

	Assert.assertFalse(root.isParentOf(root));
	Assert.assertFalse(subcontext1.isParentOf(subcontext1));
	Assert.assertFalse(subsubcontext11.isParentOf(subsubcontext11));

	Assert.assertTrue(root.isParentOf(subcontext1));
	Assert.assertTrue(root.isParentOf(subsubcontext11));
	Assert.assertTrue(root.isParentOf(subsubcontext12));
	Assert.assertTrue(root.isParentOf(subcontext2));
	Assert.assertTrue(root.isParentOf(subsubcontext21));
	Assert.assertTrue(root.isParentOf(subsubcontext22));

	Assert.assertFalse(subcontext1.isParentOf(root));
	Assert.assertTrue(subcontext1.isParentOf(subsubcontext11));
	Assert.assertTrue(subcontext1.isParentOf(subsubcontext12));
	Assert.assertFalse(subcontext1.isParentOf(subcontext2));
	Assert.assertFalse(subcontext1.isParentOf(subsubcontext21));
	Assert.assertFalse(subcontext1.isParentOf(subsubcontext22));

	Assert.assertFalse(subcontext2.isParentOf(root));
	Assert.assertFalse(subcontext2.isParentOf(subsubcontext11));
	Assert.assertFalse(subcontext2.isParentOf(subsubcontext12));
	Assert.assertFalse(subcontext2.isParentOf(subcontext1));
	Assert.assertTrue(subcontext2.isParentOf(subsubcontext21));
	Assert.assertTrue(subcontext2.isParentOf(subsubcontext22));

	Assert.assertFalse(subsubcontext11.isParentOf(subsubcontext12));
	Assert.assertFalse(subsubcontext12.isParentOf(subsubcontext11));
	Assert.assertFalse(subsubcontext21.isParentOf(subsubcontext22));
	Assert.assertFalse(subsubcontext22.isParentOf(subsubcontext21));
    }

    // TODO sharedData
    @Test
    @Ignore("sharedData to do")
    public void testSharedValue() throws Exception {
	final ContextNode subcontext = new ContextNode("subcontext");
	parent.addSubcontext(subcontext);
	final ContextNode subcontext2 = new ContextNode("subcontext2");
	parent.addSubcontext(subcontext2);

	// <element id="ref" value="value"/>
	subcontext.put(new RefId<String>("ref"), "value");
	// <elementRef id="ref" ref="../subcontext/ref"/>
	// subcontext2.put(new RefId<String>("ref"));

	Assert.assertEquals("value", subcontext.get(new RefId<String>("ref")));
	Assert.assertEquals("value", subcontext2.get(new RefId<String>("ref")));
    }

}
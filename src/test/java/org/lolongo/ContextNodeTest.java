package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContextNodeTest {


    private ContextNode parent;

    @Before
    public void initContext() {
        parent = new ContextNode("parent");
    }

    @Test
    public void testDefaultParent() throws ContextException{
      final Context context= new ContextNode("context");
      try  {
      	context.get(new RefId<String>("none"));
      } catch(RefNotFound e) {
 			Assert.assertEquals(new RefId<>("none"),e.getRef());
         Assert.assertEquals(context,e.getContext());
      }
    }

    @Test
    public void testBuildTree() throws Exception{
      final ContextNode subcontext= new ContextNode("subcontext");
      parent.addSubcontext(subcontext);
      final ContextNode subcontext2= new ContextNode("subcontext2");
      parent.addSubcontext(subcontext2);
     
      Assert.assertEquals(parent,subcontext.getParent());
      Assert.assertEquals(parent,subcontext2.getParent());
      Assert.assertEquals(2, parent.getSubcontexts().size());
      Assert.assertTrue(parent.getSubcontexts().contains(subcontext));
      Assert.assertTrue(parent.getSubcontexts().contains(subcontext2));
    }

      @Test
    public void testSubcontextUniqueByName() throws ContextAlreadyExists {
      final ContextNode subcontext= new ContextNode("child");
      parent.addSubcontext(subcontext);
      try {
      	parent.addSubcontext(new ContextNode("child"));
        	Assert.fail("Expecting ContextAlreadyExists");
      } catch(ContextAlreadyExists e) {
        Assert.assertEquals("child", e.getName());
      }
    }

    @Test
    public void testInheritance() throws Exception {
        parent.put(new RefId<String>("ref"), "val_from_parent");
        final ContextNode subcontext = new ContextNode("child");
        parent.addSubcontext(subcontext);

        Assert.assertEquals("val_from_parent", subcontext.get(new RefId<String>("ref")));
    }

    @Test
    public void testInheritanceFalse() throws Exception {
        parent.put(new RefId<String>("ref"), "val_from_parent");
        final ContextNode subcontext = new ContextNode("child", false);
        parent.addSubcontext(subcontext);

        try {
           subcontext.get(new RefId<String>("ref"));
           Assert.fail("Expecting RefNotFound");
        } catch (RefNotFound e) {
              Assert.assertEquals(new RefId<>("ref"),e.getRef());
              Assert.assertEquals(subcontext,e.getContext());
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

}
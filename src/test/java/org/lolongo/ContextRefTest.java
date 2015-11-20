package org.lolongo;

import org.junit.Assert;
import org.junit.Test;
import org.lolongo.ContextNode;

import java.util.Arrays;

public class ContextRefTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullRoot() throws Exception {
        ContextRef.getContext(null, "contextRef");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullContextRef() throws Exception {
        ContextRef.getContext(new ContextNode("root"), (String)null);
    }

    @Test
    public void testRoot() throws Exception {
        final ContextNode root = new ContextNode("root");
        Assert.assertEquals(root, ContextRef.getContext(root, "/"));
    }

    @Test
    public void test1LevelNominal() throws Exception {
        final ContextNode root = new ContextNode("root");
        final ContextNode subcontext1 = new ContextNode("subcontext1");
        root.addSubcontext(subcontext1);
        final ContextNode subcontext2 = new ContextNode("subcontext2");
        root.addSubcontext(subcontext2);

        Assert.assertEquals(root, ContextRef.getContext(root, "/"));
        Assert.assertEquals(subcontext1, ContextRef.getContext(root, "/subcontext1"));
        Assert.assertEquals(subcontext2, ContextRef.getContext(root, "/subcontext2"));
        Assert.assertEquals(subcontext1, ContextRef.getContext(root, "subcontext1"));
        Assert.assertEquals(subcontext2, ContextRef.getContext(root, "subcontext2"));
        Assert.assertEquals(subcontext1, ContextRef.getContext(root, "./subcontext1"));
        Assert.assertEquals(subcontext2, ContextRef.getContext(root, "./subcontext2"));
        Assert.assertEquals(root, ContextRef.getContext(subcontext1, "/"));
        Assert.assertEquals(subcontext1, ContextRef.getContext(subcontext1, "/subcontext1"));
        Assert.assertEquals(subcontext2, ContextRef.getContext(subcontext1, "/subcontext2"));
        Assert.assertEquals(subcontext1, ContextRef.getContext(subcontext1, "."));
        Assert.assertEquals(root, ContextRef.getContext(subcontext1, ".."));
        Assert.assertEquals(subcontext2, ContextRef.getContext(subcontext1, "../subcontext2"));
        Assert.assertEquals(root, ContextRef.getContext(subcontext2, ".."));
        Assert.assertEquals(subcontext1, ContextRef.getContext(subcontext2, "../subcontext1"));
    }

    @Test
    public void test2LevelsNominal() throws Exception {
        final ContextNode root = new ContextNode("root");
        final ContextNode subcontext1 = new ContextNode("subcontext1");
        root.addSubcontext(subcontext1);
        final ContextNode subsubcontext1 = new ContextNode("subsubcontext1");
        subcontext1.addSubcontext(subsubcontext1);
        final ContextNode subsubcontext2 = new ContextNode("subsubcontext2");
        subcontext1.addSubcontext(subsubcontext2);

        Assert.assertEquals(subsubcontext1, ContextRef.getContext(root, "subcontext1/subsubcontext1"));
        Assert.assertEquals(subsubcontext2, ContextRef.getContext(root, "subcontext1/subsubcontext2"));

        Assert.assertEquals(root, ContextRef.getContext(subsubcontext1, "/"));
        Assert.assertEquals(root, ContextRef.getContext(subsubcontext2, "/"));

        Assert.assertEquals(subsubcontext1, ContextRef.getContext(subcontext1, "/subcontext1/subsubcontext1"));
        Assert.assertEquals(subsubcontext2, ContextRef.getContext(subcontext1, "/subcontext1/subsubcontext2"));
        Assert.assertEquals(subcontext1, ContextRef.getContext(subsubcontext1, "/subcontext1"));
        Assert.assertEquals(subsubcontext1, ContextRef.getContext(subsubcontext1, "/subcontext1/subsubcontext1"));
        Assert.assertEquals(subsubcontext2, ContextRef.getContext(subsubcontext1, "/subcontext1/subsubcontext2"));
        Assert.assertEquals(subcontext1, ContextRef.getContext(subsubcontext2, "/subcontext1"));
        Assert.assertEquals(subsubcontext1, ContextRef.getContext(subsubcontext2, "/subcontext1/subsubcontext1"));
        Assert.assertEquals(subsubcontext2, ContextRef.getContext(subsubcontext2, "/subcontext1/subsubcontext2"));

        Assert.assertEquals(subsubcontext1, ContextRef.getContext(subsubcontext1, "."));
        Assert.assertEquals(subsubcontext2, ContextRef.getContext(subsubcontext2, "."));
        Assert.assertEquals(subsubcontext1, ContextRef.getContext(subcontext1, "./subsubcontext1"));
        Assert.assertEquals(subsubcontext2, ContextRef.getContext(subcontext1, "./subsubcontext2"));

        Assert.assertEquals(subcontext1, ContextRef.getContext(subsubcontext1, ".."));
        Assert.assertEquals(subcontext1, ContextRef.getContext(subsubcontext2, ".."));
        Assert.assertEquals(subsubcontext1, ContextRef.getContext(subsubcontext1, "../subsubcontext1"));
        Assert.assertEquals(subsubcontext2, ContextRef.getContext(subsubcontext2, "../subsubcontext2"));
        Assert.assertEquals(root, ContextRef.getContext(subsubcontext1, "../.."));
        Assert.assertEquals(root, ContextRef.getContext(subsubcontext2, "../.."));
    }


    @Test
    public void testGetAll() throws Exception {
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

        Assert.assertEquals(1, ContextRef.getAll(root, "subcontext1").size());
        Assert.assertTrue(ContextRef.getAll(root, "subcontext1").containsAll(Arrays.asList(subcontext1)));
        Assert.assertEquals(1, ContextRef.getAll(root, "subcontext2").size());
        Assert.assertTrue(ContextRef.getAll(root, "subcontext2").containsAll(Arrays.asList(subcontext2)));
        Assert.assertEquals(2, ContextRef.getAll(root, "subsubcontext1").size());
        Assert.assertTrue(ContextRef.getAll(root, "subsubcontext1").containsAll(Arrays.asList(subsubcontext11, subsubcontext21)));
        Assert.assertEquals(2, ContextRef.getAll(root, "subsubcontext2").size());
        Assert.assertTrue(ContextRef.getAll(root, "subsubcontext2").containsAll(Arrays.asList(subsubcontext12, subsubcontext22)));
    }

 
    @Test
    public void testGetContextRefs() throws Exception {
        Assert.assertEquals(Arrays.asList("root"), ContextRef.getContextRefList("root"));
        Assert.assertEquals(Arrays.asList("/context"), ContextRef.getContextRefList("/context"));
        Assert.assertEquals(Arrays.asList("/context/subcontext"), ContextRef.getContextRefList("/context/subcontext"));
        Assert.assertEquals(Arrays.asList("root","/context","/context/subcontext"), ContextRef.getContextRefList("root ,/context, /context/subcontext"));
    }
}

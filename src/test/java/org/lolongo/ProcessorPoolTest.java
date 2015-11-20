package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.lolongo.function.*;

public class ProcessorPoolTest {

    private ProcessorPool pool;

    @Before
    public void init() {
        pool = new ProcessorPool();
    }

    @Test
    public void testSort() throws Exception {
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


      final Processor p1 = new ProcessorBase();
      p1.setContextRef("root");
      
    }
  
     private ContextNode root;

    @Before
    public void initContext() {
        root = new ContextNode("root");
    }

    /**
     * 
     *              +----+             
     * e1-----------+    |             
     *              |    +--+--s1        
     * e2-+---------+    |  |          
     *    |         +----+  |          
     *    |                 |          
     *    |                 | +----+   
     *    |  +----+         +-+    |   
     *    +--+    |   s2      |    +---s3
     *       |    +-----------+    |   
     * e3----+    |           +----+   
     *       +----+                    
     * 
     */
  // TODO sort processor
    @Test @Ignore("TODO sort processor")
    public void test1() throws Exception {
        root.put(new RefId<Double>("e1"), 2.4);
        root.put(new RefId<Double>("e2"), 3.3);
        final ContextNode child = new ContextNode("child");
        root.addSubcontext(child);
        child.put(new RefId<Double>("e3"), -4.2);

        final Processor processor1 = new ProcessorBinding();
        processor1.add(new Addition(new RefId<Double>("e1"), new RefId<Double>("e2"), new RefId<Double>("s1")));

        final Processor processor2 = new ProcessorBinding();
        processor2.setContextRef("child");
        processor2.add(new Addition(new RefId<Double>("e2"), new RefId<Double>("e3"), new RefId<Double>("s2")));
        processor2.add(new Addition(new RefId<Double>("s1"), new RefId<Double>("s2"), new RefId<Double>("s3")));

        pool.add(processor2, processor1);
        pool.execute(root);

        Assert.assertEquals(5.7, ((Context)root).get(new RefId<Double>("s1")).doubleValue(), 0.1);
        Assert.assertEquals(-0.9, ((Context)child).get(new RefId<Double>("s2")).doubleValue(), 0.1);
        Assert.assertEquals(4.8, ((Context)child).get(new RefId<Double>("s3")).doubleValue(), 0.1);
    }

}

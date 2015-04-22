package org.lolongo;

import org.lolongo.function.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProcessorBindingContextTreeTest {

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
    @Test
    public void test1() throws Exception {
        root.put(new RefId<Double>("e1"), 2.4);
        root.put(new RefId<Double>("e2"), 3.3);
        final ContextNode child = new ContextNode("child");
        root.addSubcontext(child);
        child.put(new RefId<Double>("e3"), -4.2);

        final Processor processor1 = new ProcessorBinding();
        processor1.add(new Addition(new RefId<Double>("e1"), new RefId<Double>("e2"), new RefId<Double>("s1")));

        final Processor processor2 = new ProcessorBinding();
        processor2.setContextRef("/child");
        processor2.add(new Addition(new RefId<Double>("e2"), new RefId<Double>("e3"), new RefId<Double>("s2")));
        processor2.add(new Addition(new RefId<Double>("s1"), new RefId<Double>("s2"), new RefId<Double>("s3")));

        final ProcessorPool pool = new ProcessorPool();
        pool.add(processor2, processor1);
        pool.execute(root);
        //  Assert.assertEquals("VALUE", context.get(new RefId<String>("tmp")));
        // Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
    }
}

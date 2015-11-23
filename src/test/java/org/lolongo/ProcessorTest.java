package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.ContextBase;
import org.lolongo.FunctionException;
import org.lolongo.ProcessorBase;
import org.lolongo.RefAlreadyExists;
import org.lolongo.function.*;

public class ProcessorTest {

    private Processor processor;
    private Context   context;

    @Before
    public void initContext() {
        context = new ContextBase();
        processor = new ProcessorBase();
    }

    @Test
    public void testExecute1SimpleFunctionNominal() throws Exception {
        context.put(new RefId<String>("in"), "value");
        processor.add(new Identity(new RefId<String>("in"), new RefId<String>("out")));
        processor.execute(context);
        Assert.assertEquals("value", context.get(new RefId<String>("out")));
    }

    @Test(expected = FunctionException.class)
    public void testExecute1SimpleFunctionRefNotFound() throws Exception {
        final Function f = new Identity(new RefId<String>("in"), new RefId<String>("out"));
        processor.add(f);
        try {
            processor.execute(context);
        } catch (FunctionException e) {
            Assert.assertEquals(f, e.getFunction());
            Assert.assertTrue(e.getCause() instanceof RefNotFound);
            final RefNotFound cause = (RefNotFound)e.getCause();
            Assert.assertEquals(context, cause.getContext());
            Assert.assertEquals(new RefId<String>("in"), cause.getRef());
            throw e;
        }
    }

    @Test(expected = FunctionException.class)
    public void testExecute1SimpleFunctionRefAlreadyExists() throws Exception {
        context.put(new RefId<String>("in"), "value");
        context.put(new RefId<String>("out"), "other");
        final Function f = new Identity(new RefId<String>("in"), new RefId<String>("out"));
        processor.add(f);
        try {
            processor.execute(context);
        } catch (FunctionException e) {
            Assert.assertEquals(f, e.getFunction());
            Assert.assertTrue(e.getCause() instanceof RefAlreadyExists);
            final RefAlreadyExists cause = (RefAlreadyExists)e.getCause();
            Assert.assertEquals(context, cause.getContext());
            Assert.assertEquals(new RefId<String>("out"), cause.getRef());
            throw e;
        }
    }

    @Test
    public void testExecute2SimpleFunctionNominal() throws Exception {
        context.put(new RefId<String>("in"), "value");
        processor.add(new Identity(new RefId<String>("in"), new RefId<String>("tmp")));
        processor.add(new Identity(new RefId<String>("tmp"), new RefId<String>("out")));
        processor.execute(context);
        Assert.assertEquals("value", context.get(new RefId<String>("tmp")));
        Assert.assertEquals("value", context.get(new RefId<String>("out")));
    }

/*    @Test
    public void testSplit() throws Exception {
      final Processor p = new ProcessorBase();
      p.setContextRef("c1, c2, c3");
      final Collection<Processor> split = p.split(); 
      Assert.assertEquals(3, split.size());
      final Iterator<Processor> it = split.iterator();
      Assert.assertArrayEquals(new String[]{"c1"}, ((ProcessorBase)it.next()).getContextRef().toArray());
      Assert.assertArrayEquals(new String[]{"c2"}, ((ProcessorBase)it.next()).getContextRef().toArray());
      Assert.assertArrayEquals(new String[]{"c3"}, ((ProcessorBase)it.next()).getContextRef().toArray());
}*/

}

package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.Context;
import org.lolongo.ContextBase;
import org.lolongo.FunctionException;
import org.lolongo.ProcessingException;
import org.lolongo.ProcessorBase;
import org.lolongo.RefAlreadyExists;

public class ProcessorTest {

    private Processor   processor;
    private ContextBase context;

    @Before
    public void initContext() {
        context = new ContextBase();
        processor = new ProcessorBase();
    }

    static class Identity implements Function {
        @Override
        public void execute(Context context) throws FunctionException, ContextException {
            final String out = context.get(new RefId<String>("in"));
            context.put(new RefId<String>("out"), out);
        }
    }

    @Test
    public void testExecute1SimpleFunctionNominal() throws Exception {
        context.put(new RefId<String>("in"), "value");
        processor.add(new Identity());
        processor.execute(context);
        Assert.assertEquals("value", context.get(new RefId<String>("out")));
    }

    @Test
    public void testExecute1SimpleFunctionRefNotFound() throws Exception {
        final Function f = new Identity();
        processor.add(f);
        try {
            processor.execute(context);
            Assert.fail("Expected FunctionException");
        } catch (FunctionException e) {
            Assert.assertEquals(f, e.getFunction());
            Assert.assertTrue(e.getCause() instanceof RefNotFound);
            final RefNotFound cause = (RefNotFound)e.getCause();
            Assert.assertEquals(context, cause.getContext());
            Assert.assertEquals(new RefId<String>("in"), cause.getRef());
        }
    }

    @Test
    public void testExecute1SimpleFunctionRefAlreadyExists() throws Exception {
        context.put(new RefId<String>("in"), "value");
        context.put(new RefId<String>("out"), "other");
        final Function f = new Identity();
        processor.add(f);
        try {
            processor.execute(context);
            Assert.fail("Expected FunctionException");
        } catch (FunctionException e) {
            Assert.assertEquals(f, e.getFunction());
            Assert.assertTrue(e.getCause() instanceof RefAlreadyExists);
            final RefAlreadyExists cause = (RefAlreadyExists)e.getCause();
            Assert.assertEquals(context, cause.getContext());
            Assert.assertEquals(new RefId<String>("out"), cause.getRef());
        }
    }
}

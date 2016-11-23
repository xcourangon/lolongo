package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lolongo.function.Concatenate;
import org.lolongo.function.Identity;
import org.lolongo.function.Quote;
import org.lolongo.function.ToUpperCase;

public class ProcessorTest {

    private Processor processor;
    private Context context;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void initContext() {
        context = new ContextBase();
        processor = new ProcessorBase();
    }

    /**
     * Execute one Simple function
     */
    @Test
    public void testExecute1SimpleFunction() throws Exception {
        context.put(new RefId<String>("in"), "value");
        processor.add(new ToUpperCase(new RefId<String>("in"), new RefId<String>("out")));
        processor.execute(context);
        Assert.assertEquals("VALUE", context.get(new RefId<String>("out")));
    }

    /**
     * Execute one Simple function but input RefNotFound
     */
    @Test
    public void testExecute1SimpleFunctionRefNotFound() throws Exception {
        final Function f = new Identity(new RefId<String>("in"), new RefId<String>("out"));
        processor.add(f);
        try {
            processor.execute(context);
            thrown.expect(FunctionException.class);
        } catch (FunctionException e) {
            Assert.assertEquals(f, e.getFunction());
            Assert.assertTrue(e.getCause() instanceof RefNotFound);
            final RefNotFound cause = (RefNotFound) e.getCause();
            Assert.assertEquals(context, cause.getContext());
            Assert.assertEquals(new RefId<String>("in"), cause.getRef());
        }
    }

    /**
     * Execute one Simple function but output RefAlreadyExists
     */
    @Test
    public void testExecute1SimpleFunctionRefAlreadyExists() throws Exception {
        context.put(new RefId<String>("in"), "value");
        context.put(new RefId<String>("out"), "other");
        final Function f = new Identity(new RefId<String>("in"), new RefId<String>("out"));
        processor.add(f);
        try {
            processor.execute(context);
            thrown.expect(FunctionException.class);
        } catch (FunctionException e) {
            Assert.assertEquals(f, e.getFunction());
            Assert.assertTrue(e.getCause() instanceof RefAlreadyExists);
            final RefAlreadyExists cause = (RefAlreadyExists) e.getCause();
            Assert.assertEquals(context, cause.getContext());
            Assert.assertEquals(new RefId<String>("out"), cause.getRef());
        }
    }

    /**
     * Execute two Simple chained functions (already ordered)
     */
    @Test
    public void testExecute2SimpleChainedFunctions() throws Exception {
        context.put(new RefId<String>("in"), "value");
        processor.add(new ToUpperCase(new RefId<String>("in"), new RefId<String>("tmp")));
        processor.add(new Quote(new RefId<String>("tmp"), new RefId<String>("out")));
        processor.execute(context);
        Assert.assertEquals("VALUE", context.get(new RefId<String>("tmp")));
        Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
    }

    /**
     * Execute two Simple parallel functions (no order)
     */
    @Test
    public void testExecute2SimpleParallelFunctions() throws Exception {
        context.put(new RefId<String>("in1"), "value1");
        context.put(new RefId<String>("in2"), "value2");
        processor.add(new ToUpperCase(new RefId<String>("in1"), new RefId<String>("out1")));
        processor.add(new Quote(new RefId<String>("in2"), new RefId<String>("out2")));
        processor.execute(context);
        Assert.assertEquals("VALUE1", context.get(new RefId<String>("out1")));
        Assert.assertEquals("'value2'", context.get(new RefId<String>("out2")));
    }

    /**
     * Execute three Simple chained/parallel functions (already ordered)
     */
    @Test
    public void testExecute3SimpleFunctions() throws Exception {
        context.put(new RefId<String>("a"), "a");
        context.put(new RefId<String>("b"), "b");
        context.put(new RefId<String>("c"), "c");
        context.put(new RefId<String>("d"), "d");
        processor.add(new Concatenate(new RefId<String>("a"), new RefId<String>("b"), new RefId<String>("ab")));
        processor.add(new Concatenate(new RefId<String>("c"), new RefId<String>("d"), new RefId<String>("cd")));
        processor.add(new Concatenate(new RefId<String>("ab"), new RefId<String>("cd"), new RefId<String>("out")));
        processor.execute(context);
        Assert.assertEquals("ab", context.get(new RefId<String>("ab")));
        Assert.assertEquals("cd", context.get(new RefId<String>("cd")));
        Assert.assertEquals("abcd", context.get(new RefId<String>("out")));
    }
}

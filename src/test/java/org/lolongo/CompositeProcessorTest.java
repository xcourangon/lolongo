package org.lolongo;

import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.function.Identity;
import org.lolongo.function.Quote;
import org.lolongo.function.ToUpperAndQuote;
import org.lolongo.function.ToUpperAndQuote2;
import org.lolongo.function.ToUpperCase;

public class CompositeProcessorTest {

    private Processor processor;
    private ContextBase context;

    @Before
    public void initContext() {
	context = new ContextBase();
	processor = new CompositeProcessor();
    }

    /**
     * A CompositeFunction can be executed by a CompositeProcessor
     */
    @Test
    public void testCompositeFunction() throws Exception {
	context.put(new RefId<String>("in"), "value");
	processor.add(new ToUpperAndQuote(new RefId<String>("in"), new RefId<String>("out")));
	processor.execute(context);
	Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
    }

    /**
     * An InternalRef used within a CompositeFunction must not be found in the
     * execution Context
     */
    @Test(expected = RefNotFound.class)
    public void testInternalRefNotFound() throws Exception {
	try {
	    context.put(new RefId<String>("in"), "value");
	    processor.add(new ToUpperAndQuote(new RefId<String>("in"), new RefId<String>("out")));
	    processor.execute(context);
	} catch (final Exception e) {
	    Assert.fail();
	}
	context.get(new InternalRef<String>("tmp"));
    }

    /**
     * A CompositeFunction prepare/resolve can be executed by a
     * CompositeProcessor
     */
    @Test
    public void testCompositeFunction2() throws Exception {
	context.put(new RefId<String>("in"), "value");
	processor.add(new ToUpperAndQuote2(new RefId<String>("in"), new RefId<String>("out")));
	processor.execute(context);
	Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
    }

    /**
     * When a CompositeFunction prepare/resolve is executed by a
     * CompositeProcessor no InternalRef is visible in the execution Context.
     */
    @Test(expected = RefNotFound.class)
    public void testInternalRefNotFound2() throws Exception {
	try {
	    context.put(new RefId<String>("in"), "value");
	    processor.add(new ToUpperAndQuote2(new RefId<String>("in"), new RefId<String>("out")));
	    processor.execute(context);
	} catch (final Exception e) {
	    throw new AssertionError(e);
	}
	context.get(new InternalRef<String>("tmp"));
    }

    @Test
    public void testCompositeFunction3() throws Exception {
	context.put(new RefId<String>("in"), "value");
	processor.add(new Identity(new RefId<String>("in"), new RefId<String>("tmp")));
	processor.add(new ToUpperAndQuote2(new RefId<String>("tmp"), new RefId<String>("out")));
	processor.add(new Identity(new RefId<String>("out"), new RefId<String>("out2")));
	processor.execute(context);
	Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out2")));
    }

    @Test
    public void testPrepareCompositeFunction3() throws Exception {
	context.put(new RefId<String>("in"), "value");
	final Identity f1 = new Identity(new RefId<String>("in"), new RefId<String>("tmp"));
	processor.add(f1);
	final ToUpperAndQuote2 f2 = new ToUpperAndQuote2(new RefId<String>("tmp"), new RefId<String>("out"));
	processor.add(f2);
	final Identity f3 = new Identity(new RefId<String>("out"), new RefId<String>("out2"));
	processor.add(f3);

	final CompositeFunctionContainer chain = new CompositeFunctionContainer(context);
	((CompositeProcessor) processor).prepare(chain, context);

	int i = 0;
	Context internalContext = null;
	for (final Entry<Function, Context> entry : chain) {
	    final Function function = entry.getKey();
	    final Context c = entry.getValue();
	    switch (i) {
	    case 0:
		Assert.assertEquals(function, f1);
		Assert.assertEquals(context, c);
		break;
	    case 1:
		Assert.assertEquals(ToUpperCase.class, function.getClass());
		Assert.assertNotNull(c);
		Assert.assertNotEquals(context, c);
		Assert.assertEquals(InternalContext.class, c.getClass());
		internalContext = c;
		assert internalContext != null;
		break;
	    case 2:
		Assert.assertEquals(Quote.class, function.getClass());
		assert internalContext != null;
		Assert.assertEquals(internalContext, c);
		break;
	    case 3:
		Assert.assertEquals(function, f2);
		assert internalContext != null;
		Assert.assertEquals(internalContext, c);
		break;
	    case 4:
		Assert.assertEquals(function, f3);
		Assert.assertEquals(context, c);
		break;
	    }
	    i++;
	}
	Assert.assertEquals(5, i);
    }

    @Test(expected = RefNotFound.class)
    public void testCompositeFunction3Notfound() throws Exception {
	try {
	    context.put(new RefId<String>("in"), "value");
	    processor.add(new Identity(new RefId<String>("in"), new RefId<String>("tmp")));
	    processor.add(new ToUpperAndQuote2(new RefId<String>("tmp"), new RefId<String>("out")));
	    processor.add(new Identity(new RefId<String>("out"), new RefId<String>("out2")));
	    processor.execute(context);
	} catch (final Exception e) {
	    throw new AssertionError(e);
	}
	context.get(new InternalRef<String>("tmp"));
    }
}
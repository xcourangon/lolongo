package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lolongo.function.Quote;
import org.lolongo.function.ToUpperAndQuote;
import org.lolongo.function.ToUpperAndQuoteDyn;
import org.lolongo.function.ToUpperAndQuoteDynResolve;
import org.lolongo.matcher.RefExceptionMatcher;

/**
 * Test the ability of ProcessorBase to execute Composite functions.
 * 
 * @author xavier
 *
 */
public class CompositeFunctionTest {

	private Processor processor;
	private Context context;

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Before
	public void initContext() {
		context = new ContextBase();
		processor = new ProcessorBase();
	}

	@Test
	public void testToUpperAndQuote() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new ToUpperAndQuote(new RefId<String>("in"), new RefId<String>("out")));
		processor.execute(context);
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
		thrown.expect(RefNotFound.class);
		thrown.expect(new RefExceptionMatcher(new InternalRef<String>("tmp")));
		context.get(new InternalRef<String>("tmp"));
	}

	@Test
	public void testToUpperAndQuote2() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new ToUpperAndQuoteDyn(new RefId<String>("in"), new RefId<String>("out")));
		processor.execute(context);
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
		thrown.expect(RefNotFound.class);
		thrown.expect(new RefExceptionMatcher(new InternalRef<String>("tmp")));
		context.get(new InternalRef<String>("tmp"));
	}

	@Test
	public void testToUpperAndQuote3() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new ToUpperAndQuoteDynResolve(new RefId<String>("in"), new RefId<String>("out")));
		processor.execute(context);
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
		thrown.expect(RefNotFound.class);
		thrown.expect(new RefExceptionMatcher(new InternalRef<String>("tmp")));
		context.get(new InternalRef<String>("tmp"));
	}

	/**
	 * A CompositeProcessor can execute a mix of static and dynamic CompositeFunction.
	 * 
	 */
	@Test
	public void testMixedCompositeFunction() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new Quote(new RefId<String>("in"), new RefId<String>("tmp")));
		processor.add(new ToUpperAndQuoteDyn(new RefId<String>("tmp"), new RefId<String>("out")));
		processor.add(new Quote(new RefId<String>("out"), new RefId<String>("out2")));
		processor.execute(context);
		Assert.assertEquals("value", context.get(new RefId<String>("in")));
		Assert.assertEquals("'value'", context.get(new RefId<String>("tmp")));
		Assert.assertEquals("''VALUE''", context.get(new RefId<String>("out")));
		Assert.assertEquals("'''VALUE'''", context.get(new RefId<String>("out2")));
	}

}
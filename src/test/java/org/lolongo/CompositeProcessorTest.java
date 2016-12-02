package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lolongo.function.Quote;
import org.lolongo.function.ToUpperAndQuote;
import org.lolongo.function.ToUpperAndQuote2;
import org.lolongo.function.ToUpperAndQuote3;
import org.lolongo.function.ToUpperCase;
import org.lolongo.matcher.RefExceptionMatcher;

public class CompositeProcessorTest {

	private Processor processor;
	private Context context;

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Before
	public void initContext() {
		context = new ContextBase();
		processor = new CompositeProcessor();
	}

	/**
	 * A CompositeProcessor can execute a simple Function.
	 */
	@Test
	public void testToUpperCase() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new ToUpperCase(new RefId<String>("in"), new RefId<String>("out")));
		processor.execute(context);
		Assert.assertEquals("VALUE", context.get(new RefId<String>("out")));
	}

	/**
	 * A CompositeProcessor can execute a static CompositeFunction.
	 */
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

	/**
	 * A CompositeProcessor can execute a dynamic CompositeFunction.
	 */
	@Test
	public void testToUpperAndQuote2() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new ToUpperAndQuote2(new RefId<String>("in"), new RefId<String>("out")));
		processor.execute(context);
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
		thrown.expect(RefNotFound.class);
		thrown.expect(new RefExceptionMatcher(new InternalRef<String>("tmp")));
		context.get(new InternalRef<String>("tmp"));
	}

	@Test
	public void testToUpperAndQuote3() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new ToUpperAndQuote3(new RefId<String>("in"), new RefId<String>("out")));
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
		processor.add(new ToUpperAndQuote2(new RefId<String>("tmp"), new RefId<String>("out")));
		processor.add(new Quote(new RefId<String>("out"), new RefId<String>("out2")));
		processor.execute(context);
		Assert.assertEquals("value", context.get(new RefId<String>("in")));
		Assert.assertEquals("'value'", context.get(new RefId<String>("tmp")));
		Assert.assertEquals("''VALUE''", context.get(new RefId<String>("out")));
		Assert.assertEquals("'''VALUE'''", context.get(new RefId<String>("out2")));
	}

	/**
	 * Execute two Simple parallel functions (no order)
	 */
	@Test
	public void testExecute2SimpleFunctionsParallel() throws Exception {
		context.put(new RefId<String>("in1"), "value1");
		context.put(new RefId<String>("in2"), "value2");
		processor.add(new ToUpperCase(new RefId<String>("in1"), new RefId<String>("out1")));
		processor.add(new Quote(new RefId<String>("in2"), new RefId<String>("out2")));
		processor.execute(context);
		Assert.assertEquals("VALUE1", context.get(new RefId<String>("out1")));
		Assert.assertEquals("'value2'", context.get(new RefId<String>("out2")));
	}

	/**
	 * Execute two CompositeFunction parallel functions (no order)
	 */
	@Test
	public void testExecute2CompositeFunctionsParallel() throws Exception {
		//		processor.setFunctionSequencer(FunctionSequencerBinding.getInstance());
		context.put(new RefId<String>("in1"), "value1");
		context.put(new RefId<String>("in2"), "value2");
		processor.add(new ToUpperAndQuote(new RefId<String>("in1"), new RefId<String>("out1")));
		processor.add(new ToUpperAndQuote(new RefId<String>("in2"), new RefId<String>("out2")));
		processor.execute(context);
		Assert.assertEquals("'VALUE1'", context.get(new RefId<String>("out1")));
		Assert.assertEquals("'VALUE2'", context.get(new RefId<String>("out2")));
	}

}
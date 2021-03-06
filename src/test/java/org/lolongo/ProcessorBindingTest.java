package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.function.Addition;
import org.lolongo.function.Identity;
import org.lolongo.function.Multiplication;
import org.lolongo.function.Quote;
import org.lolongo.function.ToUpperAndQuoteDyn;
import org.lolongo.function.ToUpperCase;

public class ProcessorBindingTest {

	private Processor processor;
	private ContextBase context;

	@Before
	public void initContext() {
		context = new ContextBase();
		processor = new ProcessorBase(new FunctionSequencerBinding());
	}

	@Test
	public void test2Unordered() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new Quote(new RefId<String>("upper"), new RefId<String>("quoted")));
		processor.add(new ToUpperCase(new RefId<String>("in"), new RefId<String>("upper")));
		processor.execute(context);
		Assert.assertEquals("VALUE", context.get(new RefId<String>("upper")));
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("quoted")));
	}

	@Test
	public void test3Unordered() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new Identity(new RefId<String>("upper"), new RefId<String>("tmp")));
		processor.add(new Quote(new RefId<String>("tmp"), new RefId<String>("quoted")));
		processor.add(new ToUpperCase(new RefId<String>("in"), new RefId<String>("upper")));
		processor.execute(context);
		Assert.assertEquals("VALUE", context.get(new RefId<String>("upper")));
		Assert.assertEquals("VALUE", context.get(new RefId<String>("tmp")));
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("quoted")));
	}

	/**
	 * This processor doesn't manage CompositeFunction.
	 *
	 * Treated as an atomic Function.
	 */
	// @Test
	// public void testSortCompositeFunction() throws Exception {
	// final Collection<Function> functions = new ArrayList<>();
	// final CompositeFunction function = new ToUpperAndQuote2(new
	// RefId<String>("in"), new RefId<String>("out"));
	// functions.add(function);
	// final Collection<Function>[] sortedFunctions =
	// ProcessorBinding.sort(functions, context);
	//
	// Assert.assertEquals(1, sortedFunctions.length);
	// final Collection<Function> step1 = sortedFunctions[0];
	// Assert.assertEquals(1, step1.size());
	// Assert.assertEquals(function, step1.iterator().next());
	// }

	@Test
	public void testCompositeFunctionUnordered() throws Exception {
		context.put(new RefId<String>("in"), "value");
		processor.add(new ToUpperAndQuoteDyn(new RefId<String>("tmp"), new RefId<String>("out")));
		processor.add(new Identity(new RefId<String>("in"), new RefId<String>("tmp")));
		processor.add(new Identity(new RefId<String>("out"), new RefId<String>("out2")));
		processor.execute(context);
		Assert.assertEquals("value", context.get(new RefId<String>("in")));
		Assert.assertEquals("value", context.get(new RefId<String>("tmp")));
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out2")));
	}

	@Test
	public void test2() throws Exception {
		context.put(new RefId<Double>("a"), 12.4);
		context.put(new RefId<Double>("b"), 22.7);
		context.put(new RefId<Double>("c"), -6.9);
		processor.add(new Addition(new RefId<Double>("a"), new RefId<Double>("b"), new RefId<Double>("d")));
		processor.add(new Addition(new RefId<Double>("d"), new RefId<Double>("c"), new RefId<Double>("e")));
		processor.execute(context);
		Assert.assertEquals(35.1, context.get(new RefId<Double>("d")).doubleValue(), 0.1);
		Assert.assertEquals(28.2, context.get(new RefId<Double>("e")).doubleValue(), 0.1);
	}

	@Test
	public void test3() throws Exception {
		context.put(new RefId<Double>("a"), 2.0);
		context.put(new RefId<Double>("b"), 3.1);
		context.put(new RefId<Double>("c"), 4.2);
		context.put(new RefId<Double>("d"), -0.9);
		context.put(new RefId<Double>("e"), 3.0);
		processor.add(new Addition(new RefId<Double>("ab"), new RefId<Double>("de"), new RefId<Double>("result")));
		processor.add(new Addition(new RefId<Double>("c"), new RefId<Double>("d"), new RefId<Double>("cd")));
		processor.add(new Multiplication(new RefId<Double>("a"), new RefId<Double>("bc"), new RefId<Double>("ab")));
		processor.add(new Multiplication(new RefId<Double>("cd"), new RefId<Double>("e"), new RefId<Double>("de")));
		processor.add(new Addition(new RefId<Double>("b"), new RefId<Double>("c"), new RefId<Double>("bc")));
		processor.execute(context);
		Assert.assertEquals(14.6, context.get(new RefId<Double>("ab")).doubleValue(), 0.1);
		Assert.assertEquals(7.3, context.get(new RefId<Double>("bc")).doubleValue(), 0.1);
		Assert.assertEquals(3.3, context.get(new RefId<Double>("cd")).doubleValue(), 0.1);
		Assert.assertEquals(9.9, context.get(new RefId<Double>("de")).doubleValue(), 0.1);
		Assert.assertEquals(24.5, context.get(new RefId<Double>("result")).doubleValue(), 0.1);
	}
}

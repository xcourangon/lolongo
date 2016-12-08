package org.lolongo;

import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lolongo.function.CompositePrepare;
import org.lolongo.function.CompositeResolve;
import org.lolongo.function.CompositeStatic;
import org.lolongo.function.FunctionType1;
import org.lolongo.function.FunctionType2;

public class Sort2ChainedCompositeFunctions {

	private ContextBase context;
	private CompositeProcessor processor;
	private FunctionSequencer sorter;

	private static final RefId<String> in = new RefId<String>("in");
	private static final RefId<String> out = new RefId<String>("out");
	private static final RefId<String> tmp = new RefId<String>("tmp");
	private static final FunctionType1 FUNCTION_TYPE1 = new FunctionType1(in, tmp);
	private static final FunctionType2 FUNCTION_TYPE2 = new FunctionType2(tmp, out);

	@BeforeClass
	public static void initStatic() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Before
	public void init() {
		context = new ContextBase();
		processor = new CompositeProcessor();
		sorter = FunctionSequencerBinding.getInstance();
	}

	/**
	 * 6 steps : Ap -> a -> Ar -> Bp -> b -> Br
	 */
	@Test
	public void testSortStatic() throws Exception {

		final Function functions[] = { new CompositeStatic(in, out, FUNCTION_TYPE1), new CompositeStatic(in, out, FUNCTION_TYPE2) };

		processor.add(functions[1]);
		processor.add(functions[0]);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		Collections.shuffle(container);
		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		final Collection<Function>[] stepsWithoutContext = Composite1Simple.getStepsWithoutContext(steps);
		//		Assert.assertEquals(5, stepsWithoutContext.length);
		Collection<Function> step1 = stepsWithoutContext[0];
		Assert.assertEquals(1, step1.size());
		Assert.assertTrue(step1.contains(FUNCTION_TYPE1));
		Collection<Function> step2 = stepsWithoutContext[1];
		Assert.assertEquals(2, step2.size());
		Assert.assertTrue(step2.contains(functions[0]));
		Assert.assertTrue(step2.contains(FUNCTION_TYPE2));
		Collection<Function> step3 = stepsWithoutContext[2];
		Assert.assertEquals(1, step3.size());
		Assert.assertTrue(step3.contains(functions[1]));
	}

	/**
	 * 6 steps : Ap -> a -> Ar -> (Bp) -> b -> Br
	 */
	@Test
	public void testSortDynamic() throws Exception {

		final Function functions[] = { new CompositePrepare(in, out, FUNCTION_TYPE1), new CompositePrepare(in, out, FUNCTION_TYPE2) };

		processor.add(functions[1]);
		processor.add(functions[0]);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		Collections.shuffle(container);
		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		final Collection<Function>[] stepsWithoutContext = Composite1Simple.getStepsWithoutContext(steps);
		//		Assert.assertEquals(5, stepsWithoutContext.length);
		Collection<Function> step1 = stepsWithoutContext[0];
		Assert.assertEquals(1, step1.size());
		Assert.assertTrue(step1.contains(FUNCTION_TYPE1)); // a
		Collection<Function> step2 = stepsWithoutContext[1];
		Assert.assertEquals(2, step2.size());
		Assert.assertTrue(step2.contains(functions[0])); // Ar
		Assert.assertTrue(step2.contains(FUNCTION_TYPE2)); // b
		Collection<Function> step3 = stepsWithoutContext[2];
		Assert.assertEquals(1, step3.size());
		Assert.assertTrue(step3.contains(functions[1])); // Br
	}

	@Test
	public void testSortResolve() throws Exception {
		final Function functions[] = { new CompositeResolve(in, out, FUNCTION_TYPE1), new CompositeResolve(in, out, FUNCTION_TYPE2) };
		processor.add(functions[1]);
		processor.add(functions[0]);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		Collections.shuffle(container);
		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		final Collection<Function>[] stepsWithoutContext = Composite1Simple.getStepsWithoutContext(steps);
		//		Assert.assertEquals(5, stepsWithoutContext.length);
		Assert.assertEquals(1, stepsWithoutContext.length);
		Collection<Function> step1 = stepsWithoutContext[0];
		Assert.assertEquals(2, step1.size());
		Assert.assertTrue(step1.contains(functions[0]));
		Assert.assertTrue(step1.contains(functions[1]));
	}

	//@Theory
	public void testExecute(Function composite) throws Exception {
		processor.add(composite);
		context.put(in, "value");
		processor.execute(context);

		Assert.assertEquals("FunctionType2(FunctionType1(value))", context.get(out));
	}
}

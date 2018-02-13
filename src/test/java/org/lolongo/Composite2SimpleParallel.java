package org.lolongo;

import java.util.Collection;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.lolongo.function.CompositePrepare;
import org.lolongo.function.CompositeResolve;
import org.lolongo.function.CompositeStatic;
import org.lolongo.function.FunctionType1;
import org.lolongo.function.FunctionType2;

@RunWith(Theories.class)
public class Composite2SimpleParallel {

	private ContextBase context;
	private CompositeProcessor processor;
	private FunctionSequencer sorter;

	private static final RefId<String> in = new RefId<String>("in");
	private static final RefId<String> in2 = new RefId<String>("in2");
	private static final RefId<String> out = new RefId<String>("out");
	private static final RefId<String> out2 = new RefId<String>("out2");
	private static final FunctionType1 FUNCTION_TYPE1 = new FunctionType1(in, out);
	private static final FunctionType2 FUNCTION_TYPE2 = new FunctionType2(in2, out2);

	@BeforeClass
	public static void initStatic() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Before
	public void init() {
		context = new ContextBase();
		processor = new CompositeProcessor();
		sorter = new FunctionSequencerBinding(processor);
	}

	@DataPoints
	public static final Function composite[] = { //
			new CompositeStatic(FUNCTION_TYPE1, FUNCTION_TYPE2), //
			new CompositePrepare(FUNCTION_TYPE1, FUNCTION_TYPE2), //
			new CompositeResolve(FUNCTION_TYPE1, FUNCTION_TYPE2) //
	};

	/**
	 * A Composite Function containing two independent Simple Functions produces:
	 * 2 steps : Simple Function 1 // Simple Function 2 -> Composite Function (resolve)
	 */
	@Theory
	public void testSort(Function composite) throws Exception {
		Assume.assumeFalse(composite instanceof CompositeResolve);
		processor.add(composite);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		final Collection<Function>[] stepsWithoutContext = Composite1Simple.getStepsWithoutContext(steps);
		Assert.assertEquals(2, stepsWithoutContext.length);
		Collection<Function> step1 = stepsWithoutContext[0];
		Assert.assertEquals(2, step1.size());
		Assert.assertTrue(step1.contains(FUNCTION_TYPE1));
		Assert.assertTrue(step1.contains(FUNCTION_TYPE2));
		Collection<Function> step2 = stepsWithoutContext[1];
		Assert.assertEquals(1, step2.size());
		Assert.assertTrue(step2.contains(composite));
	}

	@Theory
	public void testSortCompositeResolve(Function composite) throws Exception {
		Assume.assumeTrue(composite instanceof CompositeResolve);
		processor.add(composite);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		final Collection<Function>[] stepsWithoutContext = Composite1Simple.getStepsWithoutContext(steps);
		Assert.assertEquals(1, stepsWithoutContext.length);
		final Collection<Function> step1 = stepsWithoutContext[0];
		Assert.assertEquals(1, step1.size());
		Assert.assertTrue(step1.contains(composite));
	}

	@Theory
	public void executionTheory(Function composite) throws Exception {
		processor.add(composite);
		context.put(in, "value");
		context.put(in2, "value2");
		processor.execute(context);

		Assert.assertEquals("FunctionType1(value)", context.get(out));
		Assert.assertEquals("FunctionType2(value2)", context.get(out2));
	}
}

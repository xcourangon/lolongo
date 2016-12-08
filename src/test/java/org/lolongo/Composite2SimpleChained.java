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
public class Composite2SimpleChained {

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

	@DataPoints
	public static final Function composite[] = { //
			new CompositeStatic(in, out, FUNCTION_TYPE1, FUNCTION_TYPE2), //
			new CompositePrepare(in, out, FUNCTION_TYPE1, FUNCTION_TYPE2), //
			new CompositeResolve(in, out, FUNCTION_TYPE1, FUNCTION_TYPE2) };

	/**
	 * A Composite Function containing two chained Simple Functions produces:
	 * 3 steps : Simple Function 1 -> Simple Function 2 -> Composite Function (resolve)
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
		Assert.assertEquals(3, stepsWithoutContext.length);
		Collection<Function> step1 = stepsWithoutContext[0];
		Assert.assertEquals(1, step1.size());
		Assert.assertTrue(step1.contains(FUNCTION_TYPE1));
		Collection<Function> step2 = stepsWithoutContext[1];
		Assert.assertEquals(1, step2.size());
		Assert.assertTrue(step2.contains(FUNCTION_TYPE2));
		Collection<Function> step3 = stepsWithoutContext[2];
		Assert.assertEquals(1, step3.size());
		Assert.assertTrue(step3.contains(composite));
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
	public void testExecute(Function composite) throws Exception {
		processor.add(composite);
		context.put(in, "value");
		processor.execute(context);

		Assert.assertEquals("FunctionType2(FunctionType1(value))", context.get(out));
	}
}

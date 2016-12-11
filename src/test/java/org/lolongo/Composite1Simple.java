package org.lolongo;

import java.util.ArrayList;
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

@RunWith(Theories.class)
public class Composite1Simple {

	private ContextBase context;
	private CompositeProcessor processor;
	private FunctionSequencer sorter;

	private static RefId<String> in = new RefId<String>("in");
	private static RefId<String> out = new RefId<String>("out");
	private static final FunctionType1 FUNCTION_TYPE1 = new FunctionType1(in, out);

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
			new CompositeStatic(in, out, FUNCTION_TYPE1), //
			new CompositePrepare(in, out, FUNCTION_TYPE1), //
			new CompositeResolve(in, out, FUNCTION_TYPE1) //
	};

	/**
	 * A Composite Function containing one Simple Function produces:
	 * 2 steps : Simple Function -> Composite Function (resolve)
	 */
	@Theory
	public void sortTheory(Function composite) throws Exception {
		Assume.assumeFalse(composite instanceof CompositeResolve);
		processor.add(composite);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		final Collection<Function>[] stepsWithoutContext = getStepsWithoutContext(steps);
		Assert.assertEquals(2, stepsWithoutContext.length);
		Collection<Function> step1 = stepsWithoutContext[0];
		Assert.assertEquals(1, step1.size());
		Assert.assertTrue(step1.contains(FUNCTION_TYPE1));
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

		final Collection<Function>[] stepsWithoutContext = getStepsWithoutContext(steps);
		Assert.assertEquals(1, stepsWithoutContext.length);
		final Collection<Function> step1 = stepsWithoutContext[0];
		Assert.assertEquals(1, step1.size());
		Assert.assertTrue(step1.contains(composite));
	}

	@Theory
	public void executeTheory(Function composite) throws Exception {
		processor.add(composite);
		context.put(in, "value");
		processor.execute(context);

		Assert.assertEquals("FunctionType1(value)", context.get(out));
	}

	public static Collection<Function>[] getStepsWithoutContext(Collection<Entry<Function, Context>>[] steps) {
		final Collection<Function> stepsWithoutContext[] = new ArrayList[steps.length];
		int i = 0;
		for (Collection<Entry<Function, Context>> step : steps) {
			stepsWithoutContext[i] = new ArrayList<>();
			for (Entry<Function, Context> entry : step) {
				Function function = entry.getKey();
				if (function instanceof ComponentFunction) {
					stepsWithoutContext[i].add(((ComponentFunction) function).getFunction());
				} else {
					stepsWithoutContext[i].add(function);

				}
			}
			i++;
		}
		assert stepsWithoutContext.length == steps.length;
		return stepsWithoutContext;
	}
}

package org.lolongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.function.Addition;
import org.lolongo.function.Identity;
import org.lolongo.function.ToUpperCase;

public class FunctionSequencerBindingTest {

	private ContextBase context;
	private FunctionSequencer sorter;

	@Before
	public void init() {
		context = new ContextBase();
		sorter = FunctionSequencerBinding.getInstance();
	}

	/**
	 * Sort two Simple chained functions (unordered)
	 */
	@Test
	public void testSort2Unordered() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Identity identity1 = new Identity(new RefId<String>("in1"), new RefId<String>("out1"));
		functions.add(identity1);
		final Identity identity2 = new Identity(new RefId<String>("out1"), new RefId<String>("out2"));
		functions.add(identity2);

		Collections.shuffle(functions);
		final Collection<Function>[] sortedFunctions = sorter.sort(functions, context);

		// 2 steps : identity1 -> identity2
		Assert.assertEquals(2, sortedFunctions.length);
		final Collection<Function> step1 = sortedFunctions[0];
		Assert.assertEquals(1, step1.size());
		Assert.assertTrue(step1.contains(identity1));

		final Collection<Function> step2 = sortedFunctions[1];
		Assert.assertEquals(1, step2.size());
		Assert.assertTrue(step2.contains(identity2));
	}

	/**
	 * Sort 3 Simple chained functions (unordered)
	 */
	@Test
	public void testSort3Unordered() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Identity identity1 = new Identity(new RefId<String>("in"), new RefId<String>("out1"));
		functions.add(identity1);
		final Identity identity2 = new Identity(new RefId<String>("out1"), new RefId<String>("out2"));
		functions.add(identity2);
		final ToUpperCase identity3 = new ToUpperCase(new RefId<String>("out2"), new RefId<String>("out3"));
		functions.add(identity3);

		Collections.shuffle(functions);
		final Collection<Function>[] sortedFunctions = sorter.sort(functions, context);

		// 3 steps : toUpperCase -> identity -> quote
		Assert.assertEquals(3, sortedFunctions.length);
		final Collection<Function> step1 = sortedFunctions[0];
		Assert.assertEquals(1, step1.size());
		Assert.assertTrue(step1.contains(identity1));

		final Collection<Function> step2 = sortedFunctions[1];
		Assert.assertEquals(1, step2.size());
		Assert.assertTrue(step2.contains(identity2));

		final Collection<Function> step3 = sortedFunctions[2];
		Assert.assertEquals(1, step3.size());
		Assert.assertTrue(step3.contains(identity3));
	}

	/**
	 * Sort two Simple chained functions (unordered)
	 */
	@Test
	public void testSort2Parallel() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Identity identity1 = new Identity(new RefId<String>("in1"), new RefId<String>("out1"));
		functions.add(identity1);
		final Identity identity2 = new Identity(new RefId<String>("in2"), new RefId<String>("out2"));
		functions.add(identity2);

		Collections.shuffle(functions);
		final Collection<Function>[] sortedFunctions = sorter.sort(functions, context);

		// 1 step : identity1 // identity2
		Assert.assertEquals(1, sortedFunctions.length);
		final Collection<Function> step1 = sortedFunctions[0];
		Assert.assertEquals(2, step1.size());
		Assert.assertTrue(step1.contains(identity1));
		Assert.assertTrue(step1.contains(identity2));
	}

	/**
	 * Sort two Simple chained functions (unordered) + 1 Simple
	 */
	@Test
	public void testSort2Chained1Simple() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Identity identity1 = new Identity(new RefId<String>("in1"), new RefId<String>("out1"));
		functions.add(identity1);
		final Identity identity2 = new Identity(new RefId<String>("out1"), new RefId<String>("out2"));
		functions.add(identity2);
		final Identity identity3 = new Identity(new RefId<String>("in3"), new RefId<String>("out3"));
		functions.add(identity3);

		Collections.shuffle(functions);
		final Collection<Function>[] steps = sorter.sort(functions, context);

		// 2 steps : identity1 // identity3 -> identity2
		Assert.assertEquals(2, steps.length);
		Assert.assertEquals(2, steps[0].size());
		Assert.assertTrue(steps[0].contains(identity1));
		Assert.assertTrue(steps[0].contains(identity3));
		Assert.assertEquals(1, steps[1].size());
		Assert.assertTrue(steps[1].contains(identity2));
	}

	/**
	 * Sort three Simple chained/parallel functions. In two steps.
	 */
	@Test
	public void testSort3Functions2Steps() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Addition add1 = new Addition(new RefId<Double>("a"), new RefId<Double>("b"), new RefId<Double>("c"));
		functions.add(add1);
		final Addition add2 = new Addition(new RefId<Double>("d"), new RefId<Double>("e"), new RefId<Double>("f"));
		functions.add(add2);
		final Addition add3 = new Addition(new RefId<Double>("c"), new RefId<Double>("f"), new RefId<Double>("g"));
		functions.add(add3);

		Collections.shuffle(functions);
		final Collection<Function>[] steps = sorter.sort(functions, context);

		// add1 // add2 -> add3
		Assert.assertEquals(2, steps.length);
		Collection<Function> step1 = steps[0];
		Assert.assertEquals(2, step1.size());
		Assert.assertTrue(step1.contains(add1));
		Assert.assertTrue(step1.contains(add2));
		Collection<Function> step2 = steps[1];
		Assert.assertEquals(1, step2.size());
		Assert.assertTrue(step2.contains(add3));
	}

	/**
	 * Sort two Simple chained functions (unordered) + 1 Simple
	 */
	@Test
	public void testSort3Chained() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Identity identity1 = new Identity(new RefId<String>("in1"), new RefId<String>("out1"));
		functions.add(identity1);
		final Identity identity2 = new Identity(new RefId<String>("out1"), new RefId<String>("out2"));
		functions.add(identity2);
		final Identity identity3 = new Identity(new RefId<String>("out1"), new RefId<String>("out3"));
		functions.add(identity3);

		Collections.shuffle(functions);
		final Collection<Function>[] steps = sorter.sort(functions, context);

		// 2 steps : identity1 -> identity2 // identity3
		Assert.assertEquals(2, steps.length);
		Assert.assertEquals(1, steps[0].size());
		Assert.assertTrue(steps[0].contains(identity1));
		Assert.assertEquals(2, steps[1].size());
		Assert.assertTrue(steps[1].contains(identity2));
		Assert.assertTrue(steps[1].contains(identity3));
	}
}

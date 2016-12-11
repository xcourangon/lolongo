package org.lolongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.function.SimpleFunction1in1out;
import org.lolongo.function.SimpleFunction1in2out;
import org.lolongo.function.SimpleFunction2in1out;

public class SimpleFunctionSequencerBindingTest {

	private static final RefId<String> IN1 = new RefId<String>("in1");
	private static final RefId<String> OUT1 = new RefId<String>("out1");
	private static final RefId<String> IN2 = new RefId<String>("in2");
	private static final RefId<String> OUT2 = new RefId<String>("out2");
	private static final RefId<String> IN3 = new RefId<String>("in3");
	private static final RefId<String> OUT3 = new RefId<String>("out3");
	private static final RefId<String> OUT4 = new RefId<String>("out4");
	private static final RefId<String> TMP1 = new RefId<String>("tmp1");
	private static final RefId<String> TMP2 = new RefId<String>("tmp2");
	private static final RefId<String> TMP3 = new RefId<String>("tmp3");
	private static final RefId<String> TMP4 = new RefId<String>("tmp4");

	private ContextBase context;
	private FunctionSequencer sorter;

	@Before
	public void init() {
		context = new ContextBase();
		sorter = new FunctionSequencerBinding();
	}

	/**
	 * Sort two chained Simple functions (unordered)
	 * 2: A -> B
	 */
	@Test
	public void testSort2Unordered() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Function identity1 = new SimpleFunction1in1out(IN1, OUT1);
		functions.add(identity1);
		final Function identity2 = new SimpleFunction1in1out(OUT1, OUT2);
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
	 * Sort 3 chained Simple functions (unordered)
	 * 3: A -> B -> C
	 */
	@Test
	public void testSort3Unordered() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Function identity1 = new SimpleFunction1in1out(IN1, OUT1);
		functions.add(identity1);
		final Function identity2 = new SimpleFunction1in1out(OUT1, OUT2);
		functions.add(identity2);
		final Function identity3 = new SimpleFunction1in1out(OUT2, OUT3);
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
	 * Sort two paralleled Simple functions (unordered)
	 * 1: A // B
	 */
	@Test
	public void testSort2Parallel() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Function identity1 = new SimpleFunction1in1out(IN1, OUT1);
		functions.add(identity1);
		final Function identity2 = new SimpleFunction1in1out(IN2, OUT2);
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
	 * Sort two chained Simple functions (unordered) + 1 Simple function (C)
	 * 2: A // C -> B or A -> B // C
	 */
	@Test
	public void testSort2Chained1Simple() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Function identity1 = new SimpleFunction1in1out(IN1, OUT1);
		functions.add(identity1);
		final Function identity2 = new SimpleFunction1in1out(OUT1, OUT2);
		functions.add(identity2);
		final Function identity3 = new SimpleFunction1in1out(IN3, OUT3);
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
	 * Sort three Simple chained/parallel functions
	 * 2: A // B -> C
	 */
	@Test
	public void testSort3Functions2Steps() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Function add1 = new SimpleFunction1in1out(IN1, OUT1);
		functions.add(add1);
		final Function add2 = new SimpleFunction1in1out(IN2, OUT2);
		functions.add(add2);
		final Function add3 = new SimpleFunction2in1out(OUT1, OUT2, OUT3);
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
	 * 2: A -> B // C
	 */
	@Test
	public void testSort3Chained() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Function identity1 = new SimpleFunction1in1out(IN1, OUT1);
		functions.add(identity1);
		final Function identity2 = new SimpleFunction1in1out(OUT1, OUT2);
		functions.add(identity2);
		final Function identity3 = new SimpleFunction1in1out(OUT1, OUT3);
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

	/**
	 * 2: A -> B // C
	 */
	@Test
	public void testSort3Chained2() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Function identity1 = new SimpleFunction1in2out(IN1, OUT1, OUT2);
		functions.add(identity1);
		final Function identity2 = new SimpleFunction1in1out(OUT1, OUT3);
		functions.add(identity2);
		final Function identity3 = new SimpleFunction1in1out(OUT2, OUT4);
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

	/**
	 * 2: A // B -> C // D
	 */
	@Test
	public void testSort4Chained() throws Exception {
		final List<Function> functions = new ArrayList<>();
		final Function identity1 = new SimpleFunction1in2out(IN1, TMP1, TMP2);
		functions.add(identity1);
		final Function identity2 = new SimpleFunction1in2out(IN2, TMP3, TMP4);
		functions.add(identity2);
		final Function identity3 = new SimpleFunction2in1out(TMP1, TMP3, OUT1);
		functions.add(identity3);
		final Function identity4 = new SimpleFunction2in1out(TMP2, TMP4, OUT2);
		functions.add(identity4);

		Collections.shuffle(functions);
		final Collection<Function>[] steps = sorter.sort(functions, context);

		// 2 steps : identity1 -> identity2 // identity3
		Assert.assertEquals(2, steps.length);
		Assert.assertEquals(2, steps[0].size());
		Assert.assertTrue(steps[0].contains(identity1));
		Assert.assertTrue(steps[0].contains(identity2));
		Assert.assertEquals(2, steps[1].size());
		Assert.assertTrue(steps[1].contains(identity3));
		Assert.assertTrue(steps[1].contains(identity4));
	}
}

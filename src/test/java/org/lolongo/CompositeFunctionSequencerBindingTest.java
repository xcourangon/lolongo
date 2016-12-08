package org.lolongo;

import java.util.Collection;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lolongo.function.Quote;
import org.lolongo.function.ToUpperAndQuote;
import org.lolongo.function.ToUpperCase;

public class CompositeFunctionSequencerBindingTest {

	private ContextBase context;
	private CompositeProcessor processor;
	private FunctionSequencer sorter;

	private static RefId<String> in1 = new RefId<String>("in1");
	private static RefId<String> in2 = new RefId<String>("in2");
	private static RefId<String> out1 = new RefId<String>("out1");
	private static RefId<String> out2 = new RefId<String>("out2");
	private static RefId<String> tmp = new RefId<String>("tmp");

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
	 * Sort two Composite functions parallel.
	 * Each Composite function contains two SimpleFunction.
	 * 
	 * Expecting 3 steps :
	 * comp1.ToUpperCase // comp2.ToUpperCase -> comp1.Quote // comp2.Quote -> comp1.resolve // comp2.resolve
	 */
	@Test
	public void testSort2CompositeFunctionsParallel() throws Exception {
		final Function comp1 = new ToUpperAndQuote(in1, out1);
		processor.add(comp1);
		final Function comp2 = new ToUpperAndQuote(in2, out2);
		processor.add(comp2);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		Assert.assertEquals(3, steps.length);
		// step 1 
		final Collection<Entry<Function, Context>> step1 = steps[0];
		Assert.assertEquals(2, step1.size());
		/*
		 		final Iterator<Entry<Function, Context>> iteratorStep1 = step1.iterator();
		 
				// f1 : RefId(in1) -> ToUpperCase -> Internal(?) in Temp(context)
				final Entry<Function, Context> step1Entry1 = iteratorStep1.next();
				Assert.assertEquals(ToUpperCase.class, step1Entry1.getKey().getClass());
				final ToUpperCase f1 = (ToUpperCase) step1Entry1.getKey();
				Assert.assertEquals(new RefId<String>("in1"), f1.refIn);
				Assert.assertEquals(InternalRef.class, f1.refOut.getClass());
				Assert.assertEquals(InternalContext.class, step1Entry1.getValue().getClass());
		
				// f2 : RefId(in1) -> ToUpperCase -> Internal(?) in Temp(context)
				final Entry<Function, Context> step1Entry2 = iteratorStep1.next();
				Assert.assertEquals(ToUpperCase.class, step1Entry2.getKey().getClass());
				final ToUpperCase f2 = (ToUpperCase) step1Entry2.getKey();
				Assert.assertEquals(new RefId<String>("in2"), f2.refIn);
				Assert.assertEquals(InternalRef.class, f2.refOut.getClass());
				Assert.assertEquals(InternalContext.class, step1Entry2.getValue().getClass());
		*/
		// step 2
		final Collection<Entry<Function, Context>> step2 = steps[1];
		Assert.assertEquals(2, step2.size());
		/*
				final Iterator<Entry<Function, Context>> iteratorStep2 = step2.iterator();
				// f3 : Internal(?) -> Quote -> RefId(in1)  in Temp(context)
				final Entry<Function, Context> step2Entry1 = iteratorStep2.next();
				Assert.assertEquals(Quote.class, step2Entry1.getKey().getClass());
				final Quote f3 = (Quote) step2Entry1.getKey();
				Assert.assertEquals(InternalRef.class, f3.refIn.getClass());
				Assert.assertEquals(new RefId<String>("out1"), f3.refOut);
				Assert.assertEquals(InternalContext.class, step2Entry1.getValue().getClass());
		
				// f4 : Internal(?) -> Quote -> RefId(in1)  in Temp(context)
				final Entry<Function, Context> step2Entry2 = iteratorStep2.next();
				Assert.assertEquals(Quote.class, step2Entry2.getKey().getClass());
				final Quote f4 = (Quote) step2Entry2.getKey();
				Assert.assertEquals(InternalRef.class, f4.refIn.getClass());
				Assert.assertEquals(new RefId<String>("out2"), f4.refOut);
				Assert.assertEquals(InternalContext.class, step2Entry2.getValue().getClass());
		*/
		// step 3
		final Collection<Entry<Function, Context>> step3 = steps[2];
		Assert.assertEquals(2, step3.size());
	}

	/**
	 * Sort two functions parallel :
	 * - one SimpleFunction and one Composite
	 * The Composite function contains two SimpleFunction.
	 * 
	 * Expecting 3 steps :
	 * ToUpperCase // comp2.ToUpperCase -> comp2.Quote -> comp2.resolve
	 */
	@Test
	public void testSort1Function1CompositeFunctionsParallel() throws Exception {
		final Function comp1 = new ToUpperCase(in1, out2);
		processor.add(comp1);
		final Function comp2 = new ToUpperAndQuote(in2, out2);
		processor.add(comp2);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		Assert.assertEquals(3, steps.length);

		// step 1 
		final Collection<Entry<Function, Context>> step1 = steps[0];
		Assert.assertEquals(2, step1.size());
		/*		
		 		final Iterator<Entry<Function, Context>> iteratorStep1 = step1.iterator();
				// ToUpperCase : RefId(in1) -> ToUpperCase -> Internal(?) in Temp(context)
				final Entry<Function, Context> step1Entry1 = iteratorStep1.next();
				Assert.assertEquals(ToUpperCase.class, step1Entry1.getKey().getClass());
				final ToUpperCase f1 = (ToUpperCase) step1Entry1.getKey();
				Assert.assertEquals(new RefId<String>("in1"), f1.refIn);
				Assert.assertEquals(new RefId<String>("out1"), f1.refOut);
				Assert.assertEquals(context, step1Entry1.getValue());
		
				// ToUpperCase : RefId(in1) -> ToUpperCase -> Internal(?) in Temp(context)
				final Entry<Function, Context> step1Entry2 = iteratorStep1.next();
				Assert.assertEquals(ToUpperCase.class, step1Entry2.getKey().getClass());
				final ToUpperCase f2 = (ToUpperCase) step1Entry2.getKey();
				Assert.assertEquals(new RefId<String>("in2"), f2.refIn);
				Assert.assertEquals(InternalRef.class, f2.refOut.getClass());
				Assert.assertEquals(InternalContext.class, step1Entry2.getValue().getClass());
		*/
		// step 2
		final Collection<Entry<Function, Context>> step2 = steps[1];
		Assert.assertEquals(1, step2.size());
		/*		
				final Iterator<Entry<Function, Context>> iteratorStep2 = step2.iterator();
		
				// Quote : Internal(?) -> Quote -> RefId(in1)  in Temp(context)
				final Entry<Function, Context> step2Entry2 = iteratorStep2.next();
				Assert.assertEquals(Quote.class, step2Entry2.getKey().getClass());
				final Quote f4 = (Quote) step2Entry2.getKey();
				Assert.assertEquals(InternalRef.class, f4.refIn.getClass());
				Assert.assertEquals(new RefId<String>("out2"), f4.refOut);
				Assert.assertEquals(InternalContext.class, step2Entry2.getValue().getClass());
		*/
		final Collection<Entry<Function, Context>> step3 = steps[2];
		Assert.assertEquals(1, step3.size());
	}

	/**
	 * Sort 3 functions parallel :
	 * - 2 chained SimpleFunction and one Composite
	 * The Composite function contains two SimpleFunction.
	 * 
	 * Expecting 3 steps :
	 * ToUpperCase // comp.ToUpperCase -> Quote // comp.Quote -> comp.resolve
	 */
	@Test
	public void testSort2Function1CompositeFunctionsParallel() throws Exception {
		final Function toUpperCase = new ToUpperCase(in1, tmp);
		processor.add(toUpperCase);
		final Function quote = new Quote(tmp, out1);
		processor.add(quote);
		final Function comp = new ToUpperAndQuote(in2, out2);
		processor.add(comp);

		// prepare functions
		CompositeFunctionContainer container = new CompositeFunctionContainer(context);
		processor.prepare(container, context);

		final Collection<Entry<Function, Context>>[] steps = sorter.sort(container);

		Assert.assertEquals(3, steps.length);

		// step 1 
		final Collection<Entry<Function, Context>> step1 = steps[0];
		Assert.assertEquals(2, step1.size());
		/*
		final Iterator<Entry<Function, Context>> iteratorStep1 = step1.iterator();
		// ToUpperCase : RefId(in1) -> ToUpperCase -> RefId(tmp) in context
		final Entry<Function, Context> step1Entry1 = iteratorStep1.next();
		Assert.assertEquals(ToUpperCase.class, step1Entry1.getKey().getClass());
		final ToUpperCase fToUpperCase = (ToUpperCase) step1Entry1.getKey();
		Assert.assertEquals(new RefId<String>("in1"), fToUpperCase.refIn);
		Assert.assertEquals(new RefId<String>("tmp"), fToUpperCase.refOut);
		Assert.assertEquals(context, step1Entry1.getValue());
		
		// ToUpperCase : RefId(in1) -> ToUpperCase -> Internal(?) in Temp(context)
		final Entry<Function, Context> step1Entry2 = iteratorStep1.next();
		Assert.assertEquals(ToUpperCase.class, step1Entry2.getKey().getClass());
		final ToUpperCase comp1ToUpperCase = (ToUpperCase) step1Entry2.getKey();
		Assert.assertEquals(new RefId<String>("in2"), comp1ToUpperCase.refIn);
		Assert.assertEquals(InternalRef.class, comp1ToUpperCase.refOut.getClass());
		Assert.assertEquals(InternalContext.class, step1Entry2.getValue().getClass());
		*/
		// step 2
		final Collection<Entry<Function, Context>> step2 = steps[1];
		Assert.assertEquals(2, step2.size());
		/*
				final Iterator<Entry<Function, Context>> iteratorStep2 = step2.iterator();
				// Quote : RefId(tmp) -> Quote -> RefId(out2) in context
				final Entry<Function, Context> step2Entry1 = iteratorStep2.next();
				Assert.assertEquals(Quote.class, step2Entry1.getKey().getClass());
				final Quote fQuote = (Quote) step2Entry1.getKey();
				Assert.assertEquals(new RefId<String>("tmp"), fQuote.refIn);
				Assert.assertEquals(new RefId<String>("out2"), fQuote.refOut);
				Assert.assertEquals(context, step2Entry1.getValue());
		
				// Quote : Internal(?) -> Quote -> RefId(in1)  in Temp(context)
				final Entry<Function, Context> step2Entry2 = iteratorStep2.next();
				Assert.assertEquals(Quote.class, step2Entry2.getKey().getClass());
				final Quote comp2Quote = (Quote) step2Entry2.getKey();
				Assert.assertEquals(InternalRef.class, comp2Quote.refIn.getClass());
				Assert.assertEquals(new RefId<String>("out2"), comp2Quote.refOut);
				Assert.assertEquals(InternalContext.class, step2Entry2.getValue().getClass());
		*/

		// step 3
		final Collection<Entry<Function, Context>> step3 = steps[2];
		Assert.assertEquals(1, step3.size());
		final Entry<Function, Context> step3entry = step3.iterator().next();
		Assert.assertEquals(comp, step3entry.getKey());
	}

}

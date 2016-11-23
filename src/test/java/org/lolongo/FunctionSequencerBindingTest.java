package org.lolongo;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.function.Addition;
import org.lolongo.function.Identity;
import org.lolongo.function.Quote;
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
        final Quote quote = new Quote(new RefId<String>("upper"), new RefId<String>("quoted"));
        functions.add(quote);
        final ToUpperCase toUpperCase = new ToUpperCase(new RefId<String>("in"), new RefId<String>("upper"));
        functions.add(toUpperCase);
        final Collection<Entry<Function, Context>>[] sortedFunctions = sorter.sort(functions, context);

        Assert.assertEquals(2, sortedFunctions.length);
        final Collection<Entry<Function, Context>> step1 = sortedFunctions[0];
        Assert.assertEquals(1, step1.size());
        Entry<Function, Context> next = step1.iterator().next();
        Assert.assertEquals(toUpperCase, next.getKey());
        Assert.assertEquals(context, next.getValue());

        final Collection<Entry<Function, Context>> step2 = sortedFunctions[1];
        Assert.assertEquals(1, step2.size());
        Assert.assertTrue(step2.contains(new AbstractMap.SimpleEntry<>(quote, context)));
    }

    /**
     * Sort 3 Simple chained functions (unordered)
     */
    @Test
    public void testSort3Unordered() throws Exception {
        final List<Function> functions = new ArrayList<>();
        final Identity identity = new Identity(new RefId<String>("upper"), new RefId<String>("tmp"));
        functions.add(identity);
        final Quote quote = new Quote(new RefId<String>("tmp"), new RefId<String>("quoted"));
        functions.add(quote);
        final ToUpperCase toUpperCase = new ToUpperCase(new RefId<String>("in"), new RefId<String>("upper"));
        functions.add(toUpperCase);

        final Collection<Entry<Function, Context>>[] sortedFunctions = sorter.sort(functions, context);

        Assert.assertEquals(3, sortedFunctions.length);
        final Collection<Entry<Function, Context>> step1 = sortedFunctions[0];
        Assert.assertEquals(1, step1.size());
        Assert.assertTrue(step1.contains(new AbstractMap.SimpleEntry<>(toUpperCase, context)));

        final Collection<Entry<Function, Context>> step2 = sortedFunctions[1];
        Assert.assertEquals(1, step2.size());
        Assert.assertTrue(step2.contains(new AbstractMap.SimpleEntry<>(identity, context)));

        final Collection<Entry<Function, Context>> step3 = sortedFunctions[2];
        Assert.assertEquals(1, step3.size());
        Assert.assertTrue(step3.contains(new AbstractMap.SimpleEntry<>(quote, context)));
    }

    /**
     * Sort two Simple parallel functions. Both are in the same step.
     */
    @Test
    public void testSort2Functions1Step() throws Exception {
        final List<Function> functions = new ArrayList<>();
        final Addition add1 = new Addition(new RefId<Double>("a"), new RefId<Double>("b"), new RefId<Double>("c"));
        functions.add(add1);
        final Addition add2 = new Addition(new RefId<Double>("a"), new RefId<Double>("d"), new RefId<Double>("e"));
        functions.add(add2);
        final Collection<Entry<Function, Context>>[] steps = sorter.sort(functions, context);
        Assert.assertEquals(1, steps.length);
        Collection<Entry<Function, Context>> step = steps[0];
        Assert.assertEquals(2, step.size());
        Assert.assertTrue(step.contains(new AbstractMap.SimpleEntry<>(add1, context)));
        Assert.assertTrue(step.contains(new AbstractMap.SimpleEntry<>(add2, context)));
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
        final Collection<Entry<Function, Context>>[] steps = sorter.sort(functions, context);
        Assert.assertEquals(2, steps.length);
        Collection<Entry<Function, Context>> step1 = steps[0];
        Assert.assertEquals(2, step1.size());
        Assert.assertTrue(step1.contains(new AbstractMap.SimpleEntry<>(add1, context)));
        Assert.assertTrue(step1.contains(new AbstractMap.SimpleEntry<>(add2, context)));
        Collection<Entry<Function, Context>> step2 = steps[1];
        Assert.assertEquals(1, step2.size());
        Assert.assertTrue(step2.contains(new AbstractMap.SimpleEntry<>(add3, context)));
    }
}

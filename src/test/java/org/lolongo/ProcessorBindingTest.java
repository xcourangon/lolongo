package org.lolongo;

import org.lolongo.function.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProcessorBindingTest {

    private Processor   processor;
    private ContextBase context;

    @Before
    public void initContext() {
        context = new ContextBase();
        processor = new ProcessorBinding();
    }

    @Test
    public void test1() throws Exception {
        context.put(new RefId<String>("in"), "value");
        processor.add(new Quote(new RefId<String>("tmp"), new RefId<String>("out")));
        processor.add(new ToUpperCase(new RefId<String>("in"), new RefId<String>("tmp")));
        processor.execute(context);
        Assert.assertEquals("VALUE", context.get(new RefId<String>("tmp")));
        Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
    }
  
  /**
   *  a -- 
   * 			d = a + b = 12.4 + 22.7 = 35.1 --
   *  b --
   *                                          e = d + c = 35.1 - 6.9 = 28.2
   *  c --------------
   * 
   */ 
    @Test
    public void test2() throws Exception {
        context.put(new RefId<Double>("a"), 12.4);
        context.put(new RefId<Double>("b"), 22.7);
        context.put(new RefId<Double>("c"), -6.9);
        processor.add(new Addition(new RefId<Double>("a"), new RefId<Double>("b"), new RefId<Double>("d")));
        processor.add(new Addition(new RefId<Double>("d"), new RefId<Double>("c"), new RefId<Double>("e")));
        processor.execute(context);
        Assert.assertEquals(35.1, context.get(new RefId<Double>("d")).doubleValue(),0.1);
        Assert.assertEquals(28.2, context.get(new RefId<Double>("e")).doubleValue(),0.1);
    }
  
  /**
   * a=2 
   *            *  14.6
   * b=3.1 
   *      + 7.3
   * c=4.2                +   24.5
   *      + 3.3
   * d=-0.9
   *            * 9.9
   * e=3
   */ 
    @Test
    public void test3() throws Exception {
        context.put(new RefId<Double>("a"), 2.0);
        context.put(new RefId<Double>("b"), 3.1);
        context.put(new RefId<Double>("c"), 4.2);
        context.put(new RefId<Double>("d"),-0.9);
        context.put(new RefId<Double>("e"), 3.0);
        processor.add(new Addition(new RefId<Double>("ab"), new RefId<Double>("de"), new RefId<Double>("result")));
        processor.add(new Addition(new RefId<Double>("c"), new RefId<Double>("d"), new RefId<Double>("cd")));
        processor.add(new Multiplication(new RefId<Double>("a"), new RefId<Double>("bc"), new RefId<Double>("ab")));
        processor.add(new Multiplication(new RefId<Double>("cd"), new RefId<Double>("e"), new RefId<Double>("de")));
        processor.add(new Addition(new RefId<Double>("b"), new RefId<Double>("c"), new RefId<Double>("bc")));
        processor.execute(context);
        Assert.assertEquals(14.6, context.get(new RefId<Double>("ab")).doubleValue(),0.1);
        Assert.assertEquals(7.3, context.get(new RefId<Double>("bc")).doubleValue(),0.1);
        Assert.assertEquals(3.3, context.get(new RefId<Double>("cd")).doubleValue(),0.1);
        Assert.assertEquals(9.9, context.get(new RefId<Double>("de")).doubleValue(),0.1);
        Assert.assertEquals(24.5, context.get(new RefId<Double>("result")).doubleValue(),0.1);
    }

}

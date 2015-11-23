package org.lolongo;

import org.lolongo.function.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CompositeFunctionTest {

    private Processor   processor;
    private ContextBase context;

    @Before
    public void initContext() { 
        context = new ContextBase();
        processor = new ProcessorBase();
    }

    @Test
    public void testComposeManually() throws Exception {
        context.put(new RefId<String>("in"), "value");
        processor.add(new ToUpperCase(new RefId<String>("in"), new RefId<String>("tmp")));
        processor.add(new Quote(new RefId<String>("tmp"), new RefId<String>("out")));
        processor.execute(context);
        Assert.assertEquals("VALUE", context.get(new RefId<String>("tmp")));
        Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
    }
  
    @Test
    public void testCompositeFunction() throws Exception {
      context.put(new RefId<String>("in"), "value");
      processor.add(new ToUpperAndQuote(new RefId<String>("in"), new RefId<String>("out")));
      processor.execute(context);
      Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
    }

    @Test(expected=RefNotFound.class) 
    public void testInternalRefNotFound() throws Exception {
      try {
      context.put(new RefId<String>("in"), "value");
      processor.add(new ToUpperAndQuote(new RefId<String>("in"), new RefId<String>("out")));
      processor.execute(context);
      } catch(Exception e) {
        Assert.fail();
      }
      context.get(new InternalRef<String>("tmp"));
    }
    @Test
    public void testCompositeFunction2() throws Exception {
      context.put(new RefId<String>("in"), "value");
      processor.add(new ToUpperAndQuote2(new RefId<String>("in"), new RefId<String>("out")));
      processor.execute(context);
      Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
    }

    @Test(expected=RefNotFound.class) 
    public void testInternalRefNotFound2() throws Exception {
      try {
      context.put(new RefId<String>("in"), "value");
      processor.add(new ToUpperAndQuote2(new RefId<String>("in"), new RefId<String>("out")));
      processor.execute(context);
      } catch(Exception e) {
        Assert.fail();
      }
      context.get(new InternalRef<String>("tmp"));
    }
}
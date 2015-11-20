package org.lolongo;

import org.lolongo.function.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
  
  // TODO
  @Ignore
    @Test//(expected=RefNotFound.class) 
    public void testComposeAuto() throws Exception {
      context.put(new RefId<String>("in"), "value");
      processor.add(new ToUpperAndQuote(new RefId<String>("in"), new RefId<String>("out")));
      processor.execute(context);
      Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));

      //context.get(new InternalRef<String>("tmp"));
    }
}
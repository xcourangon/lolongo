package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lolongo.function.FunctionType1;
import org.lolongo.function.FunctionType2;

public class ProcessorChainTest {

    private ProcessorChain   processor;
    private ContextBase context;

    @Before
    public void initContext() {
        context = new ContextBase();
        processor = new ProcessorChain(FunctionType1.class);
        processor.add(new ProcessorChain(FunctionType2.class));
    }

    @Test
    public void testExecute2SimpleFunctionNominal() throws Exception {
        context.put(new RefId<String>("in1"), "value1");
        context.put(new RefId<String>("in2"), "value2");
        processor.add(new FunctionType1(new RefId<String>("in1"), new RefId<String>("out1")));
        processor.add(new FunctionType2(new RefId<String>("in2"), new RefId<String>("out2")));
        processor.execute(context);
        Assert.assertEquals("FunctionType1(value1)", context.get(new RefId<String>("out1")));
        Assert.assertEquals("FunctionType2(value2)", context.get(new RefId<String>("out2")));
    }

}

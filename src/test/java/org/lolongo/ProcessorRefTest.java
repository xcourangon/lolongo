package org.lolongo;

import org.junit.Assert;
import org.junit.Test;

public class ProcessorRefTest {

    @Test
    public void testToString() {
        Assert.assertEquals("ProcessorBase(contextRefs=[/context1, subcontext1, subcontext2])",
                new ProcessorRef(new ProcessorBase(), "/context1,subcontext1,subcontext2").toString());
    }
}

package org.lolongo;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class InternalContextTest {

    private Context         context;
    private InternalContext internalContext;

    @Before
    public void initContext() {
        context = new ContextBase();
        internalContext = new InternalContext(context);
    }

    @Test
    public void testInternalContext() throws Exception {
        internalContext.put(new InternalRef<String>("internalRef"), "Value");
        Assert.assertEquals("Value", internalContext.get(new InternalRef<String>("internalRef")));
    }

    @Test(expected = RefNotFound.class)
    public void testInternalContext2() throws Exception {
        internalContext.put(new InternalRef<String>("internalRef"), "Value");
        context.get(new InternalRef<String>("internalRef"));
    }

    @Test
    public void testInternalContext3() throws Exception {
        internalContext.put(new RefId<String>("ref1"), "Value");
        Assert.assertEquals("Value", internalContext.get(new RefId<String>("ref1")));
        Assert.assertEquals("Value", context.get(new RefId<String>("ref1")));
    }

}

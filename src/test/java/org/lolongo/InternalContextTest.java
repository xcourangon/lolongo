package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InternalContextTest {

    private Context context;
    private InternalContext internalContext;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void initContext() {
        context = new ContextBase();
        internalContext = new InternalContext(context);
    }

    @Test
    public void testInternalContext() throws Exception {
        internalContext.put(new InternalRef<String>("internalRef"), "Value");
        Assert.assertEquals("Value", internalContext.get(new InternalRef<String>("internalRef")));
        thrown.expect(RefNotFound.class);
        context.get(new InternalRef<String>("internalRef"));
    }

    @Test
    public void testInternalContextFiltering() throws Exception {
        internalContext.put(new RefId<String>("ref1"), "Value");
        Assert.assertEquals("Value", internalContext.get(new RefId<String>("ref1")));
        Assert.assertEquals("Value", context.get(new RefId<String>("ref1")));
    }
}

package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ContextBaseTest {

    private Context context;

    @Before
    public void initContext() {
        context = new ContextBase();
    }

    @Test(expected=RefNotFound.class)
    public void testRefNotFound() throws Exception {
        try {
            context.get(new RefId<String>("ref1"));
        } catch (RefNotFound e) {
          Assert.assertEquals(new RefId<>("ref1"),e.getRef());
          throw e;
        }
    }

    @Test
    public void testPutGet() throws Exception {

        context.put(new RefId<String>("ref1"), "Value_1");
        final String result = context.get(new RefId<String>("ref1"));
        Assert.assertEquals("Value_1", result);
    }

    @Test
    public void testPutGet2() throws RefAlreadyExists, RefNotFound {

        context.put(new RefId<String>("ref1"), "Value_1");
        context.put(new RefId<String>("ref2"), "Value_2");
        Assert.assertEquals("Value_1", context.get(new RefId<String>("ref1")));
        Assert.assertEquals("Value_2", context.get(new RefId<String>("ref2")));
    }

    @Test(expected = RefAlreadyExists.class)
    public void testRefUniqueInContext() throws RefAlreadyExists, RefNotFound {

        context.put(new RefId<String>("ref1"), "Value_1");
        try {
            context.put(new RefId<String>("ref1"), "Value_2");
        } catch (RefAlreadyExists e) {
            Assert.assertEquals(new RefId<String>("ref1"), e.getRef());
            throw e;
        } finally {
            Assert.assertEquals("Value_1", context.get(new RefId<String>("ref1")));
        }
    }

}

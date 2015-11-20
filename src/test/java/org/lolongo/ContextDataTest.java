package org.lolongo;

import org.lolongo.data.StringData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContextDataTest {

    static class ContextDataBase extends ContextBase implements ContextData { }
  
    private ContextData context;

    @Before
    public void initContext() {
        context = new ContextDataBase() ;
    }

    @Test
    public void testPutGet() throws Exception {

        context.put(new StringData(new RefId<StringData>("ref1"), "Value_1"));
        final String result = context.get(new RefId<StringData>("ref1")).getValue();
        Assert.assertEquals("Value_1", result);
    }

    @Test
    public void testPutGetNoMismatch() throws RefAlreadyExists, RefNotFound {

        context.put(new StringData(new RefId<StringData>("ref1"), "Value_1"));
        context.put(new StringData(new RefId<StringData>("ref2"), "Value_2"));
        Assert.assertEquals("Value_1", context.get(new RefId<StringData>("ref1")).getValue());
        Assert.assertEquals("Value_2", context.get(new RefId<StringData>("ref2")).getValue());
    }

    @Test(expected = RefAlreadyExists.class)
    public void testRefUniqueInContext() throws RefAlreadyExists, RefNotFound {

        context.put(new StringData(new RefId<StringData>("ref1"), "Value_1"));
        try {
            context.put(new StringData(new RefId<StringData>("ref1"), "Value_2"));
        } catch (RefAlreadyExists e) {
            Assert.assertEquals(new RefId<String>("ref1"), e.getRef());
            throw e;
        } finally {
            Assert.assertEquals("Value_1", context.get(new RefId<StringData>("ref1")).getValue());
        }
    }

}

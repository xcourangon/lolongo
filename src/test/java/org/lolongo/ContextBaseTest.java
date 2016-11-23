package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lolongo.matcher.RefExceptionMatcher;

public class ContextBaseTest {

    private Context context;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void initContext() {
        context = new ContextBase();
    }

    @Test
    public void testRefNotFoundEmptyContext() throws Exception {

        thrown.expect(RefNotFound.class);
        thrown.expect(new RefExceptionMatcher(new RefId<>("ref1")));
        context.get(new RefId<>("ref1"));
    }

    @Test
    public void testRefNotFound() throws Exception {

        context.put(new RefId<String>("otherRef"), "Value_1");
        thrown.expect(RefNotFound.class);
        thrown.expect(new RefExceptionMatcher(new RefId<>("ref1")));
        context.get(new RefId<>("ref1"));
    }

    @Test
    public void testPutGet() throws Exception {

        context.put(new RefId<String>("ref1"), "Value_1");
        final String result = context.get(new RefId<String>("ref1"));
        Assert.assertEquals("Value_1", result);
    }

    @Test
    public void testPutGetNoMismatch() throws RefAlreadyExists, RefNotFound {

        context.put(new RefId<String>("ref1"), "Value_1");
        context.put(new RefId<String>("ref2"), "Value_2");
        Assert.assertEquals("Value_1", context.get(new RefId<String>("ref1")));
        Assert.assertEquals("Value_2", context.get(new RefId<String>("ref2")));
    }

    @Test
    public void testRefUniqueInContext() throws RefAlreadyExists, RefNotFound {

        context.put(new RefId<String>("ref1"), "Value_1");
        try {
            context.put(new RefId<String>("ref1"), "Value_2");
            thrown.expect(RefAlreadyExists.class);
        } catch (RefAlreadyExists e) {
            Assert.assertEquals(new RefId<String>("ref1"), e.getRef());
            Assert.assertEquals("Value_1", context.get(new RefId<String>("ref1")));
        }
    }

}

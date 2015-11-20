package org.lolongo;

import org.junit.Assert;
import org.junit.Test;

public class NamedContextTest {

    @Test
    public void testName() throws Exception {

        final NamedContext context = new NamedContext("name_4_Test");
        Assert.assertEquals("name_4_Test", context.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartWithNum() throws Exception {

        new NamedContext("4name");
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testContainsPonct() throws Exception {

        new NamedContext("name!");
    }
}

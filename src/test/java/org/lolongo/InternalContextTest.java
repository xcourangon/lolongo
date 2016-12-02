package org.lolongo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lolongo.function.ToUpperAndQuote;
import org.lolongo.matcher.RefExceptionMatcher;

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

	/**
	 * An Internal Ref put into an Internal Context doesn't go in the parent Context.
	 */
	@Test
	public void testInternalContext() throws Exception {
		internalContext.put(new InternalRef<String>("internalRef"), "Value");
		Assert.assertEquals("Value", internalContext.get(new InternalRef<String>("internalRef")));
		thrown.expect(RefNotFound.class);
		thrown.expect(new RefExceptionMatcher(new InternalRef<String>("internalRef")));
		context.get(new InternalRef<String>("internalRef"));
	}

	/**
	 * A non-Internal Ref put into an Internal Context goes into the parent Context.
	 */
	@Test
	public void testInternalContextFiltering() throws Exception {
		internalContext.put(new RefId<String>("ref1"), "Value");
		Assert.assertEquals("Value", internalContext.get(new RefId<String>("ref1")));
		Assert.assertEquals("Value", context.get(new RefId<String>("ref1")));
	}

	@Test
	public void testInternalContextInFunction() throws Exception {
		final Processor processor = new ProcessorBase();
		context.put(new RefId<String>("in"), "value");
		processor.add(new ToUpperAndQuote(new RefId<String>("in"), new RefId<String>("out")));
		processor.execute(context);
		Assert.assertEquals("'VALUE'", context.get(new RefId<String>("out")));
		thrown.expect(RefNotFound.class);
		thrown.expect(new RefExceptionMatcher(new InternalRef<String>("tmp")));
		context.get(new InternalRef<String>("tmp"));
	}

}

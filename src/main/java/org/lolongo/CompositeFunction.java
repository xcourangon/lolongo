package org.lolongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A CompositeFunction is a Function implemented using function composition.
 *
 * @author Xavier Courangon
 */
public abstract class CompositeFunction extends FunctionContainer implements Function {

    private static final Logger logger = LoggerFactory.getLogger(CompositeFunction.class);

    // /**
    // * Adapter class allowing to use Function as CompositeFunction
    // *
    // * @author xcourang
    // *
    // */
    // Experimental
    // static class Adapter extends CompositeFunction {
    //
    // private final Function adapted;
    //
    // public Adapter(Function adapted) {
    // this.adapted = adapted;
    // }
    //
    // @Override
    // public void resolve(Context context) throws FunctionException,
    // ContextException {
    // adapted.execute(context);
    // }
    // }

    /* (non-Javadoc)
     * @see org.lolongo.Function#execute(org.lolongo.Context)
     */
    @Override
    public final void execute(Context context) throws FunctionException, ContextException {
	logger.debug("Executing Composite function...");
	CompositeFunction.execute(this, context);
    }

    public static void execute(final CompositeFunction compositeFunction, Context context)
	    throws FunctionException, ContextException {
	final Context internalContext = new InternalContext(context);
	ProcessorBase.execute(compositeFunction, internalContext);
	final FunctionContainer container = new FunctionContainer();
	compositeFunction.prepare(container, internalContext);
	ProcessorBase.execute(container, internalContext);
	compositeFunction.resolve(internalContext);
    }

    public void prepare(FunctionContainer container, Context context) throws FunctionException, ContextException {
    }

    public void resolve(Context context) throws FunctionException, ContextException {
    }

}

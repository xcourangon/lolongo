package org.lolongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A CompositeFunction is a Function implemented using function composition.
 *
 * @author Xavier Courangon
 */
public abstract class CompositeFunctionDyn extends CompositeFunction {

	private static final Logger logger = LoggerFactory.getLogger(CompositeFunctionDyn.class);

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
		final Context internalContext = new InternalContext(context);
		// TODO Need to choose the FunctionSequencer
		ProcessorBase.execute(this, internalContext);
		final FunctionContainer container = new FunctionContainer();
		logger.debug("Preparing {} on {}...", this, internalContext);
		prepare(container, internalContext);
		logger.debug("Executing {} on {}...", container, internalContext);
		// TODO Need to choose the FunctionSequencer
		ProcessorBase.execute(container, internalContext);
		logger.debug("Resolving {} on {}...", this, internalContext);
		resolve(internalContext);
	}

	public void prepare(FunctionContainer container, Context context) throws FunctionException, ContextException {
	}

	public void resolve(Context context) throws FunctionException, ContextException {
	}

	//    public void onError(Context context, FunctionException cause) throws FunctionException {
	//        throw cause;
	//    }
}

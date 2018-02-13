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
	public void execute(Context context) throws FunctionException, ContextException {
		logger.debug("Executing Composite function...");
		final Context internalContext = new InternalContext(context);
		// TODO Need to choose the FunctionSequencer
		ProcessorBase.execute(this, internalContext);
	}

	//	public void onError(Context context, FunctionException cause) throws FunctionException {
	//		throw cause;
	//	}
}

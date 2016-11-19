package org.lolongo;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositeProcessor extends ProcessorBase {

	private static final Logger logger = LoggerFactory.getLogger(CompositeProcessor.class);

	// Experimental
	// static class FunctionContainer2 extends FunctionContainer {
	//
	// public void add(CompositeFunction compositeFunction) {
	// super.add(compositeFunction);
	// }
	//
	// @Override
	// public void add(Function function) {
	// super.add(new CompositeFunction.Adapter(function));
	// }
	// }

	public void prepare(CompositeFunctionContainer container, Context context) throws FunctionException {
		logger.debug("preparing functions for {} in {}", this, context);
		for (final Function function : functions) {
			try {
				// TODO rework to avoid 'instanceof' and the inner 'for'
				if (function instanceof CompositeFunction) {
					final CompositeFunction compositeFunction = ((CompositeFunction) function);
					final InternalContext internalContext = new InternalContext(context);
					container.add((CompositeFunction) function, internalContext);

					logger.debug("- preparing Composite function {}", function);
					final FunctionContainer functionContainer = new FunctionContainer();
					compositeFunction.prepare(functionContainer, internalContext);
					container.add(functionContainer, internalContext);
					container.add(function, internalContext);
				} else {
					container.add(function);
				}
			} catch (final ContextException e) {
				throw new FunctionException(function, e);
			}
		}
	}

	public void resolve(final CompositeFunctionContainer all, Context context) throws FunctionException {
		for (final Entry<Function, Context> entry : all) {
			final Function function = entry.getKey();
			try {
				final Context internalContext = entry.getValue();
				// TODO rework to avoid 'instanceof'
				if (function instanceof CompositeFunction) {
					final CompositeFunction compositeFunction = ((CompositeFunction) function);
					logger.debug("- resolving Composite function {} in {}", compositeFunction, internalContext);
					compositeFunction.resolve(internalContext);
				} else {
					logger.debug("- executing function {} in {}", function, internalContext);
					function.execute(internalContext);
				}
			} catch (final ContextException e) {
				throw new FunctionException(function, e);
			}
		}
	}

	@Override
	public void execute(Context context) throws FunctionException {
		logger.debug("Executing {} on {}...", this, context);
		final CompositeFunctionContainer chain = new CompositeFunctionContainer(context);
		prepare(chain, context);
		resolve(chain, context);
	}

}

package org.lolongo;

import java.util.Collection;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositeProcessor extends ProcessorBase {

	private static final Logger logger = LoggerFactory.getLogger(CompositeProcessor.class);

	private FunctionSequencer functionSequencer = new SimpleFunctionSequencer();

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

	public CompositeProcessor() {
	}

	public CompositeProcessor(FunctionSequencer functionSequencer) {
		this.functionSequencer = functionSequencer;
	}

	public void prepare(CompositeFunctionContainer container, Context context) throws FunctionException {
		logger.debug("preparing functions for {} in {}", container, context);
		for (final Function function : functions) {
			try {
				// TODO rework to avoid 'instanceof' and the inner 'for'
				if (function instanceof CompositeFunction) {
					final CompositeFunction compositeFunction = ((CompositeFunction) function);
					final InternalContext internalContext = new InternalContext(context);
					container.add(compositeFunction, internalContext);

					logger.debug("- preparing Composite function {}", function);
					final FunctionContainer functionContainer = new FunctionContainer();
					compositeFunction.prepare(functionContainer, internalContext);
					for (Function compositeFunctionDyn : functionContainer) {
						container.add(new ComponentFunction(compositeFunction, compositeFunctionDyn), internalContext);
					}
					container.add(function, internalContext);
				} else {
					container.add(function);
				}
			} catch (final ContextException e) {
				throw new FunctionException(function, e);
			}
		}
	}

	public void resolve(final Collection<Entry<Function, Context>> entries) throws FunctionException {
		logger.debug("resolving {}", entries);
		for (final Entry<Function, Context> entry : entries) {
			final Function function = entry.getKey();
			try {
				final Context internalContext = entry.getValue();
				// TODO rework to avoid 'instanceof'
				if (function instanceof CompositeFunction) {
					final CompositeFunction compositeFunction = ((CompositeFunction) function);
					logger.debug("- resolving Composite function {} on {}", compositeFunction, internalContext);
					compositeFunction.resolve(internalContext);
				} else {
					logger.debug("- executing {} on {}", function, internalContext);
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
		// List<Entry<Function, Context>> chain
		final CompositeFunctionContainer chain = new CompositeFunctionContainer(context);
		prepare(chain, context);
		final Collection<Entry<Function, Context>>[] sort = functionSequencer.sort(chain);
		for (Collection<Entry<Function, Context>> collection : sort) {
			resolve(collection);
		}

	}

}

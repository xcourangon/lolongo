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

    public void prepare(CompositeFunctionContainer container, Context context) throws FunctionException, ContextException {
        logger.debug("preparing functions from {}", this);
        for (final Function function : functions) {
            // TODO rework to avoid 'instanceof' and the inner 'for'
            if (function instanceof CompositeFunction) {
                final CompositeFunction compositeFunction = ((CompositeFunction)function);
                final InternalContext internalContext = new InternalContext(context);
                container.add((CompositeFunction)function, internalContext);

                logger.debug("- preparing Composite function {}", function);
                final FunctionContainer functionContainer = new FunctionContainer();
                compositeFunction.prepare(functionContainer, internalContext);
                container.add(functionContainer, internalContext);
                container.add(function, internalContext);
            } else {
                container.add(function);
            }
        }
    }

    public void resolve(final CompositeFunctionContainer all, Context context) throws FunctionException, ContextException {
        for (final Entry<Function, Context> entry : all) {
            // TODO rework to avoid 'instanceof'
            final Function function = entry.getKey();
            final Context internalContext = entry.getValue();
            if (function instanceof CompositeFunction) {
                final CompositeFunction compositeFunction = ((CompositeFunction)function);
                logger.debug("- resolving Composite function {}", compositeFunction);
                compositeFunction.resolve(internalContext);
            } else {
                logger.debug("- executing function {}", function);
                function.execute(internalContext);
            }
        }
    }

    @Override
    public void execute(Context context) throws FunctionException, ContextException {
        logger.debug("Executing {} on {}...", this, context);
        final CompositeFunctionContainer chain = new CompositeFunctionContainer(context);
        prepare(chain, context);
        resolve(chain, context);
    }

}

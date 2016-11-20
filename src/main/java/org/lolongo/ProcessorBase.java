package org.lolongo;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation of Processor. Functions are executed in the order they were added.
 *
 * @author Xavier Courangon
 */
public class ProcessorBase extends FunctionContainer implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(ProcessorBase.class);

    protected SimpleFunctionSequencer functionSequencer = new SimpleFunctionSequencer();

    public ProcessorBase() {
    }

    public ProcessorBase(SimpleFunctionSequencer functionSequencer) {
        setFunctionSequencer(functionSequencer);
    }

    public void setFunctionSequencer(SimpleFunctionSequencer functionSequencer) {
        this.functionSequencer = functionSequencer;
    }

    /*
    @Override
    public void execute(Context... contexts) throws FunctionException, ContextNotFound {
        for (Context context : contexts) {
            execute(context);
        }
    }
     */
    
    public void execute(Collection<Context> contexts) throws FunctionException, ContextNotFound {
        for (Context context : contexts) {
            execute(context);
        }
    }

    @Override
    public void execute(Context context) throws FunctionException {
        logger.debug("executing {} on {}...", this, context);

        final Collection<Function>[] sortedFunctions = functionSequencer.sort(functions, context);
        execute(sortedFunctions, context);
    }

    protected static void execute(Collection<Function>[] functions, Context context) throws FunctionException {
        for (Collection<Function> collection : functions) {
            execute(collection, context);
        }
    }

    /**
     * Base implementation only execute functions
     */
    public static void execute(Collection<Function> functions, Context context) throws FunctionException {

        for (final Function function : functions) {
            execute(function, context);
        }
    }

    public static void execute(FunctionContainer functions, Context context) throws FunctionException {
        for (final Function function : functions) {
            execute(function, context);
        }
    }

    private static void execute(Function function, Context context) throws FunctionException {
        try {
            logger.debug("{} is executing {} on {}", function, context);
            function.execute(context);
        } catch (ContextException e) {
            throw new FunctionException(function, e);
        }
    }
}

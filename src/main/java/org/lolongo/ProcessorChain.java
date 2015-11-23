package org.lolongo;

import org.lolongo.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor implementation when Function execution must be delegated in a chain-of-responsability.
 * 
 * @author Xavier Courangon
 */
public class ProcessorChain extends ProcessorBase {


    private static final Logger logger = LoggerFactory.getLogger(ProcessorChain.class);

    private final Class< ? >    functionType;

    private ProcessorChain      successor;

    public ProcessorChain(Class< ? > functionType) {
        this.functionType = functionType;
    }

    public void add(ProcessorChain processor) {
        if (successor == null) {
            successor = processor;
        } else {
            successor.add(processor);
        }
    }

    @Override
    public void add(Function function) {
        if (functionType.isInstance(function)) {
            logger.debug("{} isInstance {}", functionType.getSimpleName(), function.getClass().getSimpleName());
            super.add(function);
        } else {
            logger.debug("{} isNotInstance {}", functionType.getSimpleName(), function.getClass().getSimpleName());
            if (successor != null) {
                logger.debug("try on {}", successor);
                successor.add(function);
            }
        }
    }

    @Override
    public void execute(Context context) throws FunctionException, ContextException {
        super.execute(context);
        if (successor != null) {
            successor.execute(context);
        }
    }


    public String toString() {
        final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append("(");
        sb.append(functionType);
        sb.append(")");
        return sb.toString();
    }
}

package org.lolongo;

import org.lolongo.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A CompositeFunction is a Function implemented using function composition.
 * 
 * @author Xavier Courangon
 */
public abstract class CompositeFunction extends FunctionContainer implements Function {

    private static final Logger logger = LoggerFactory.getLogger(CompositeFunction.class);

    @Override
    public void execute(Context context) throws FunctionException, ContextException {
        logger.debug("Executing Composite function...");
        final Context internalContext = new InternalContext(context);
        for (Function function : functions) {
            logger.debug("- Executing {}", function);
            function.execute(internalContext);
        }
    }
  
    public void prepare(Processor processor, Context context) {
    }

    public void resolve(Context context) {
    }
}

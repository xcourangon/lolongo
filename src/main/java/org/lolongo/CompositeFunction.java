package org.lolongo;

import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.FunctionException;

/**
 * A CompositeFunction is a Function implemented using function composition.
 * 
 * @author Xavier Courangon
 */
public abstract class CompositeFunction extends FunctionContainer implements Function {

    @Override
    public void execute(Context context) throws FunctionException, ContextException {

    }
}

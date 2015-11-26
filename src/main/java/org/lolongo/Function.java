package org.lolongo;

/**
 * A Function is the computation element.
 *
 * @author Xavier Courangon
 */
public interface Function {

    /**
     * Executes the function in a given Context.
     *
     * @param context the execution context where the function get and set its
     *            computation data
     * @throws FunctionException
     * @throws ContextException
     */
    void execute(Context context) throws FunctionException, ContextException;

}

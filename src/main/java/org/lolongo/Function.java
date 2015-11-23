package org.lolongo;

/**
 * A Function is the computation element.
 * 
 * @author Xavier Courangon
 */
public interface Function {

    void execute(Context context) throws FunctionException, ContextException;
  
}

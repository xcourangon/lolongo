package org.lolongo;

/**
 * A Processor executes its Functions in a Context.
 * 
 * @author Xavier Courangon
 */
public interface Processor {

    void add(Function f);

    void execute(Context context) throws FunctionException;

    // void setContextRef(String contextRef) throws IllegalArgumentException;

    // Collection<String> getContextRef();
}

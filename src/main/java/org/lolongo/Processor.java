package org.lolongo;

/**
 * A Processor executes Functions in some Contexts.
 * 
 * @author Xavier Courangon
 */
public interface Processor {

    void add(Function f);

    void execute(Context... context) throws FunctionException, ContextNotFound;

    void setContextRef(String contextRef) throws IllegalArgumentException;
}
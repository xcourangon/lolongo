package org.lolongo;

/**
 * Exception thrown when a Context already exists.
 * 
 * @author Xavier Courangon
 */
public class ContextAlreadyExists extends NamedContextException {

    public ContextAlreadyExists(String name) {
        super(name);
    }
}

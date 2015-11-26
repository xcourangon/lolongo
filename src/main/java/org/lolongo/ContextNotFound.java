package org.lolongo;

/**
 * Exception thrown when a Context was not found.
 *
 * @author Xavier Courangon
 */
public class ContextNotFound extends NamedContextException {

    public ContextNotFound(String name) {
	super(name);
    }

}
package org.lolongo;

/**
 * Exception thrown when a Ref was not found in a Context.
 * 
 * @author Xavier Courangon
 */
public class RefNotFound extends RefException {

    public RefNotFound(Ref< ? > ref, Context context) {
        super(ref, context);
    }
}

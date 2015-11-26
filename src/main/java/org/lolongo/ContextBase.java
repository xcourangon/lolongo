package org.lolongo;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation of Context.
 *
 * @author Xavier Courangon
 */
public class ContextBase implements Context {

    private static Logger logger = LoggerFactory.getLogger(ContextBase.class);

    private final HashMap<Ref<?>, Object> content = new HashMap<>();

    @Override
    public <T, R extends Ref<T>> T get(R ref) throws RefNotFound {
	logger.debug("getting {} in {}", ref, this);
	if (content.containsKey(ref)) {
	    return (T) content.get(ref);
	} else {
	    throw new RefNotFound(ref, this);
	}
    }

    @Override
    public <T, R extends Ref<T>> void put(R ref, T value) throws RefAlreadyExists {
	logger.debug("put \"{}\" at {} in {}", value, ref, this);
	if (content.containsKey(ref) == false) {
	    content.put(ref, value);
	} else {
	    throw new RefAlreadyExists(ref, this);
	}
    }

    @Override
    public String toString() {
	return "Context";
    }
}

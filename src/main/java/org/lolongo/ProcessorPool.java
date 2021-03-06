package org.lolongo;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A pool of Processor.
 *
 * @author Xavier Courangon
 */
public class ProcessorPool {

    private static final Logger logger = LoggerFactory.getLogger(ProcessorPool.class);

    private final Collection<Processor> pool = new ArrayList<>();

    public void add(Processor... processors) {
	if (processors == null) {
	    throw new IllegalArgumentException("processors is null");
	}
	for (final Processor processor : processors) {
	    logger.debug("add {} into {}", processor, this);
	    pool.addAll(split(processor));
	}
    }

    // @Override
    public static Collection<Processor> split(Processor p) {
	final Collection<Processor> split = new ArrayList<>();
	// if (contextRefs == null) {
	// split.add(this);
	// } else {
	/*        for (String contextRef : p.getContextRef()) {
	  try {
	    final Processor clone = p.getClass().newInstance();
	    clone.setContextRef(contextRef);
	    split.add(clone);
	  } catch (InstantiationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	}*/
	// }
	return split;
    }

    public void execute(Context context) throws FunctionException, ContextNotFound {
	logger.debug("executing {} on {}...", this, context);

	logger.debug("preparing functions...", this, context);
	for (final Processor processor : pool) {
	    for (final Function f : (FunctionContainer) processor) {
		logger.debug("preparing {} for {}", f, processor);
	    }
	}
    }
}

package org.lolongo;

import org.lolongo.Context;
import org.lolongo.ContextNotFound;
import org.lolongo.Function;
import org.lolongo.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Base implementation of Processor.
 * Functions are executed in the order they were added.
 * 
 * @author Xavier Courangon
 */
public class ProcessorBase extends FunctionContainer implements Processor, Cloneable {

    private static final Logger  logger = LoggerFactory.getLogger(ProcessorBase.class);

    protected Collection<String> contextRefs = Arrays.asList(".");

    @Override
    public void execute(Context... contexts) throws FunctionException, ContextNotFound {
        for (Context context : contexts) {
            execute(context);
        }
    }

    public void execute(Context context) throws FunctionException, ContextNotFound {
        logger.debug("Executing {} on {}...", this, context);
        if (contextRefs == null) {
            execute(functions, context);
        } else {
            for (String contextRef : contextRefs) {
                context = ContextRef.getContext(context, contextRef);
                execute(functions, context);
            }
        }
    }

    public void execute(Collection<Function> functions, Context context) throws FunctionException {
        for (Function function : functions) {
            execute(function, context);
        }
    }

    public void execute(Function function, Context context) throws FunctionException {
        try {
          logger.debug("{} is executing {} on {}",this, function,context);
            function.execute(context);
        } catch (ContextException e) {
            throw new FunctionException(function, e);
        }
    }

    @Override
    public void setContextRef(String contextRefList) throws IllegalArgumentException {
        if (contextRefList == null) {
            throw new IllegalArgumentException("contextRefs is null");
        }
        this.contextRefs = ContextRef.getContextRefList(contextRefList);
        assert this.contextRefs != null;
    }

    @Override
    public Collection<String> getContextRef() {
        return Collections.unmodifiableCollection(contextRefs);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        if (contextRefs != null) {
            final Iterator<String> it = contextRefs.iterator();
            sb.append("(contextRefs=");
            for (int i = 1; i < contextRefs.size(); i++) {
                assert it.hasNext();
                sb.append(it.next());
                sb.append(", ");
            }
            assert it.hasNext();
            sb.append(it.next());
            assert it.hasNext() == false;
            sb.append(")");
        }
        return sb.toString();
    }
}

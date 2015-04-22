package org.lolongo;

import org.lolongo.Context;
import org.lolongo.ContextNotFound;
import org.lolongo.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Base implementation of Processor.
 * Functions are executed in the order they were added.
 * 
 * @author Xavier Courangon
 */
public class ProcessorBase extends FunctionContainer implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(ProcessorBase.class);

    protected String            contextRef;

    @Override
    public void execute(Context... contexts) throws FunctionException, ContextNotFound {
        for (Context context : contexts) {
            execute(context);
        }
    }

    public void execute(Context context) throws FunctionException, ContextNotFound {
        logger.debug("Executing {} on {}...", this, context);
        if (contextRef != null) {
          context = ContextRef.getContext(context, contextRef);
        }

        execute(functions, context);
    }

    public void execute(Collection<Function> functions, Context context) throws FunctionException {
        for (Function function : functions) {
            execute(function, context);
        }
    }

    public void execute(Function function, Context context) throws FunctionException {
        try {
            function.execute(context);
        } catch (ContextException e) {
            throw new FunctionException(function, e);
        }
    }

    @Override
    public void setContextRef(String contextRef) throws IllegalArgumentException {
        ContextRef.check(contextRef);
        this.contextRef = contextRef;
    }
  
    @Override
	 public String toString() {
    	final StringBuffer sb=new StringBuffer(getClass().getSimpleName());
      if(contextRef!=null) {
        sb.append("(contextRef=");
        sb.append(contextRef);
        sb.append(")");
      }
      return sb.toString();
  	 }
}

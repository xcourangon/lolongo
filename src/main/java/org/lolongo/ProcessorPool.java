package org.lolongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A pool of Processor.
 * 
 * @author Xavier Courangon
 */
public class ProcessorPool {

  private static final Logger logger = LoggerFactory.getLogger(ProcessorPool.class);
  
   private final Collection<Processor> pool = new ArrayList<>();

   public void add(Processor... processors) {
     if(processors==null) {
       throw new IllegalArgumentException("processors is null");
     }
     for (Processor processor : processors) {
       logger.debug("add {} into {}",processor,this);
       pool.add(processor);
     }
   }
  
  public void execute(Context context) throws FunctionException, ContextNotFound {
    logger.debug("executing {} on {}...",this,context);
    for (Processor processor : pool) {
      processor.execute(context);
    }
  }
}

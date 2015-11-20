package org.lolongo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * A container of Functions.
 * 
 * @author Xavier Courangon
 */
public abstract class FunctionContainer {

    private static Logger logger  = LoggerFactory.getLogger(FunctionContainer.class);

    final Collection<Function> functions = new ArrayList<>();
  
    public void add(Function function) {
       if (function == null) {
			throw new IllegalArgumentException("function is null");
       } else {
         //TODO rework this to avoid 'instance of'
         //if(function instanceof CompositeFunction) {
         //  logger.debug("add CompositeFunction {} into {}",function,this);
         //  	functions.addAll(((CompositeFunction)function).functions);
         //} else {
           logger.debug("add Function {} into {}",function,this);
           functions.add(function);
         //}
       }
    }

  public void add(CompositeFunction functions) {
       if (functions == null) {
			throw new IllegalArgumentException("functions is null");
       } else {
	       Collections.addAll(this.functions, functions);
       }
    }
}

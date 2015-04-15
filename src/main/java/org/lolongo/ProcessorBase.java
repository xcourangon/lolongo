package org.lolongo;

import org.lolongo.Context;
import org.lolongo.Function;

import java.util.ArrayList;
import java.util.Collection;

public class ProcessorBase implements Processor {

  final Collection<Function> functions = new ArrayList<>();
  
    @Override
    public void add(Function function) {
       if (function == null) {
			throw new IllegalArgumentException("function is null");
       } else {
         functions.add(function);
       }
    }

    @Override
    public void execute(Context... contexts) throws  FunctionException {
      for (Context context : contexts) {
        for (Function function : functions) {
          try {
 			   function.execute(context);
          } catch(Exception e) {
            throw new FunctionException(function, e);
          }
		  }
      }
    }
}

package org.lolongo;

public interface Processor {
  
  void add(Function f);
  
  void execute(Context... context) throws FunctionException;
}

package org.lolongo;

public interface Function {
  
  void execute(Context context) throws FunctionException, ContextException;
}

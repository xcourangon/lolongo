package org.lolongo;

public class FunctionException extends Exception {

    private final Function function;

    public FunctionException(Function function, Exception cause) {
        super(cause);
        this.function = function;
    }

    public FunctionException(Function function, String message) {
        super(message);
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public String getMessage() {
      final StringBuffer sb = new StringBuffer("Error in function ");
      sb.append(function);
      if(super.getMessage()!=null) {
       sb. append(super.getMessage());
      }
      return sb.toString();
    }
}

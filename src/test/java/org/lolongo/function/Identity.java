package org.lolongo.function;

import org.lolongo.*;

public class Identity implements Function {

  @InputBinding
  private Ref< ? > refIn  = new RefId<String>("in");
  
  @OutputBinding
  private Ref< ? > refOut = new RefId<String>("out");

  public <T> Identity(Ref<T> in, Ref<T> out) {
    this.refIn = in;
    this.refOut = out;
  }

  @Override
  public void execute(Context context) throws FunctionException, ContextException {
    context.put((Ref)refOut, context.get(refIn));
  }
}
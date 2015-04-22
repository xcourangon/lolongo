package org.lolongo.function;

import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;
import org.lolongo.RefId;

public class ToUpperCase implements Function {

  @InputBinding
  private Ref< String > refIn  = new RefId<String>("in");
  
  @OutputBinding
  private Ref< String > refOut = new RefId<String>("out");

  public ToUpperCase(Ref<String> in, Ref<String> out) {
    this.refIn = in;
    this.refOut = out;
  }

  @Override
  public void execute(Context context) throws FunctionException, ContextException {
    context.put(refOut, context.get(refIn).toUpperCase());
  }
}
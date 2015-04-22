package org.lolongo.function;

import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class Addition implements Function {

  @InputBinding
  private Ref< Double > refA;
  @InputBinding
  private Ref< Double > refB;
  
  @OutputBinding
  private Ref<Double > refResult;

  public Addition(Ref<Double> a, Ref<Double> b, Ref<Double> result) {
    this.refA = a;
    this.refB = b;
    this.refResult = result;
  }

  @Override
  public void execute(Context context) throws FunctionException, ContextException {
    context.put(refResult, context.get(refA) + context.get(refB));
  }
  
  @Override
  public String toString() {
    final StringBuffer sb=new StringBuffer("Addition(");
    sb.append(refA);    
    sb.append(",");    
    sb.append(refB);
    sb.append(")=>");    
    sb.append(refResult);
    return sb.toString();
  };
}
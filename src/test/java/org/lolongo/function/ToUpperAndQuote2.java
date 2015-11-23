package org.lolongo.function;


import org.lolongo.CompositeFunction;
import org.lolongo.OutputBinding;
import org.lolongo.Processor;
import org.lolongo.Ref;
import org.lolongo.InputBinding;
import org.lolongo.InternalRef;

public class ToUpperAndQuote2 extends CompositeFunction {

    @InputBinding
    private Ref<String> refIn;

    @OutputBinding
    private Ref<String> refOut;

    public ToUpperAndQuote2(Ref<String> in, Ref<String> out) {
      this.refIn=in;
      this.refOut=out;
    }

  public void prepare(Processor processor, org.lolongo.Context context) {
     processor.add(new ToUpperCase(refIn, new InternalRef<String>("tmp")));
     processor.add(new Quote(new InternalRef<String>("tmp"), refOut));        

  };
    public String toString() {
      return getClass().getSimpleName();
    };
}
 
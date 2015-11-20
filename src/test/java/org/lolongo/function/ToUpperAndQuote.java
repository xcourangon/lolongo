package org.lolongo.function;


import org.lolongo.CompositeFunction;
import org.lolongo.Context;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;
import org.lolongo.InputBinding;
import org.lolongo.InternalRef;


public class ToUpperAndQuote extends CompositeFunction {

    @InputBinding
    private Ref<String> refIn;

    @OutputBinding
    private Ref<String> refOut;

  	 public ToUpperAndQuote() {
    
    }
  
    public ToUpperAndQuote(Ref<String> in, Ref<String> out) {
        this.refIn = in;
        this.refOut = out;
    }
 
    public void prepare(Context context) {
        add(new ToUpperCase(refIn, new InternalRef<String>("tmp")));
        add(new Quote(new InternalRef<String>("tmp"), refOut));        
    }

    public void resolve(Context context) {
      
    }


}
 
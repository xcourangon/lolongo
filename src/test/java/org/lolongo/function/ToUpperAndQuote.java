package org.lolongo.function;


import org.lolongo.CompositeFunction;
import org.lolongo.Ref;
import org.lolongo.InternalRef;

public class ToUpperAndQuote extends CompositeFunction {

    public ToUpperAndQuote(Ref<String> in, Ref<String> out) {
        add(new ToUpperCase(in, new InternalRef<String>("tmp")));
        add(new Quote(new InternalRef<String>("tmp"), out));        
    }
  
    public String toString() {
      return getClass().getSimpleName();
    };
}
 
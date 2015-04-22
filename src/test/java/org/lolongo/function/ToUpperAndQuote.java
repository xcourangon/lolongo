package org.lolongo.function;


import org.lolongo.CompositeFunction;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;
import org.lolongo.RefId;
import org.lolongo.InputBinding;
import org.lolongo.InternalRef;

public class ToUpperAndQuote extends CompositeFunction {

    @InputBinding
    private Ref<String> refIn  = new RefId<String>("in");

    @OutputBinding
    private Ref<String> refOut = new RefId<String>("out");

    public ToUpperAndQuote(Ref<String> in, Ref<String> out) {
        this.refIn = in;
        this.refOut = out;
        add(new ToUpperCase(refIn, new InternalRef<String>("tmp")));
        add(new Quote(new InternalRef<String>("tmp"), refOut));
    }
}

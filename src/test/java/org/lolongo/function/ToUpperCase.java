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
    public Ref<String> refIn = new RefId<String>("in");

    @OutputBinding
    public Ref<String> refOut = new RefId<String>("out");

    public ToUpperCase(Ref<String> in, Ref<String> out) {
        refIn = in;
        refOut = out;
    }

    @Override
    public void execute(Context context) throws FunctionException, ContextException {
        context.put(refOut, context.get(refIn).toUpperCase());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append("(");
        sb.append("in=");
        sb.append(refIn);
        sb.append(",");
        sb.append("out=");
        sb.append(refOut);
        sb.append(")");
        return sb.toString();
    }
}
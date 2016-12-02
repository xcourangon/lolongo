package org.lolongo.function;

import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class Identity implements Function {

    @InputBinding
    private Ref<?> refIn;

    @OutputBinding
    private Ref<?> refOut;

    public <T> Identity(Ref<T> in, Ref<T> out) {
        refIn = in;
        refOut = out;
    }

    @Override
    public void execute(Context context) throws FunctionException, ContextException {
        context.put((Ref) refOut, context.get(refIn));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("(in=");
        sb.append(refIn);
        sb.append(",out=");
        sb.append(refOut);
        sb.append(")");
        return sb.toString();
    }
}
package org.lolongo.function;

import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;
import org.lolongo.RefId;

public class Identity implements Function {

    @InputBinding
    private Ref<?> refIn = new RefId<String>("in");

    @OutputBinding
    private Ref<?> refOut = new RefId<String>("out");

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
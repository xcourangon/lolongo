package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Context context) throws FunctionException, ContextException {
		context.put((Ref) refOut, context.get(refIn));
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
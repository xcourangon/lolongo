package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleFunction1in1out implements Function {

	@InputBinding
	private Ref<?> in;

	@OutputBinding
	private Ref out;

	public <T> SimpleFunction1in1out(Ref<T> in, Ref<T> out) {
		this.in = in;
		this.out = out;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		context.put(out, context.get(in));
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	};
}
package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class SimpleFunction2in1out implements Function {

	@InputBinding
	private Ref<?> in1;
	@InputBinding
	private Ref<?> in2;

	@OutputBinding
	private Ref<String> out;

	public <T> SimpleFunction2in1out(Ref<T> in1, Ref<T> in2, Ref<String> out) {
		this.in1 = in1;
		this.in2 = in2;
		this.out = out;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		context.put(out, context.get(in1).toString() + context.get(in2).toString());
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	};
}
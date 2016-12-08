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
public class SimpleFunction2in2out implements Function {

	@InputBinding
	private Ref<?> in1;
	@InputBinding
	private Ref<?> in2;

	@OutputBinding
	private Ref out1;
	@OutputBinding
	private Ref out2;

	public <T, U> SimpleFunction2in2out(Ref<T> in1, Ref<U> in2, Ref<T> out1, Ref<U> out2) {
		this.in1 = in1;
		this.in2 = in2;
		this.out1 = out1;
		this.out2 = out2;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		context.put(out1, context.get(in1));
		context.put(out2, context.get(in2));
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	};
}
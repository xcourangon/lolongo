package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class Concatenate implements Function {

	@InputBinding
	private Ref<String> refA;
	@InputBinding
	private Ref<String> refB;

	@OutputBinding
	private Ref<String> refResult;

	public Concatenate(Ref<String> a, Ref<String> b, Ref<String> result) {
		this.refA = a;
		this.refB = b;
		this.refResult = result;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		context.put(refResult, context.get(refA) + context.get(refB));
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	};
}
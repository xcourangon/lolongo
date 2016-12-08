package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;
import org.lolongo.RefId;

public class FunctionType2 implements Function {

	@InputBinding
	private Ref<String> refIn = new RefId<String>("in");

	@OutputBinding
	private Ref<String> refOut = new RefId<String>("out");

	public FunctionType2(Ref<String> in, Ref<String> out) {
		this.refIn = in;
		this.refOut = out;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		context.put(refOut, "FunctionType2(" + context.get(refIn) + ")");
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
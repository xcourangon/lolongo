package org.lolongo;

import java.util.List;

public class ProcessorRef implements Processor {

    private final ProcessorBase processor;
    private final List<String> contextRefList;

    public ProcessorRef(ProcessorBase processor, String contextRefs) {
        this.processor = processor;
        this.contextRefList = ContextRef.getContextRefList(contextRefs);
    }

    @Override
    public void add(Function f) {
        processor.add(f);
    }

    @Override
    public void execute(Context context) throws FunctionException {
        try {
            processor.execute(ContextRef.getAllContext((ContextNode) context, contextRefList));
        } catch (ClassCastException | ContextNotFound cause) {
            throw new IllegalArgumentException(cause);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(processor.toString());
        sb.append("(contextRefs=");
        sb.append(contextRefList);
        sb.append(")");
        return sb.toString();
    }
}

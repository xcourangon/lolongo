package org.lolongo;

import org.lolongo.ProcessingException;

public interface ProcessorPool {

    void add(Processor processor);

    public void execute() throws ProcessingException;

}

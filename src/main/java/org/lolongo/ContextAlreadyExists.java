package org.lolongo;

public class ContextAlreadyExists extends NamedContextException {

    public ContextAlreadyExists(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "Context named "+getName() + " already exists";
    }
}

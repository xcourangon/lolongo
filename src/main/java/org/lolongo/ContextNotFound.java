package org.lolongo;

public class ContextNotFound extends NamedContextException {
  

    public ContextNotFound(String name) {
        super(name);
    }

    public String getMessage() {
        return "Context " + getName() + "not found";
    };
}
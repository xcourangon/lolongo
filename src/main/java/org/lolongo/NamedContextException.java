package org.lolongo;

public abstract class NamedContextException extends Exception {

    private String name;

    public NamedContextException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

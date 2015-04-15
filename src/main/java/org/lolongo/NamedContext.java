package org.lolongo;

public class NamedContext extends ContextBase {

    private final String name;

    public NamedContext(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Context named " + name;
    }
}

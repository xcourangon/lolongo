package org.lolongo;


/**
 * A Context having a name. Useful to retrieve a Context among other Contexts.
 * 
 * @author Xavier Courangon
 */
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
        return "Context " + name;
    }
}

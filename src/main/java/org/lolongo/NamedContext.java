package org.lolongo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Context having a name. Useful to retrieve a Context among other Contexts.
 * 
 * @author Xavier Courangon
 */
public class NamedContext extends ContextBase {

    public static final String regex = "[a-zA-Z_][a-zA-Z_0-9]*";
    private static final Pattern pattern = Pattern.compile(regex);

    private final String name;

    public NamedContext(String name) throws IllegalArgumentException {
        checkName(name);
        this.name = name;
    }

    protected void checkName(String name) throws IllegalArgumentException {
        final Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("'" + name + "' is not a correct Context name");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Context '" + name + "'";
    }
}

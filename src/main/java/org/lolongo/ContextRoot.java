package org.lolongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context Root for Context tree.
 *
 * @author Xavier Courangon
 */
public class ContextRoot extends ContextNode {

    private static Logger logger = LoggerFactory.getLogger(ContextRoot.class);

    @Override
    protected void checkName(String name) {
        assert name == null;
    }

    public ContextRoot() {
        super(null);
    }

    @Override
    public ContextNode getParent() {
        return null;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public ContextRoot getRoot() {
        return this;
    }
}

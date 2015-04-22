package org.lolongo;

import org.lolongo.Context;
import org.lolongo.ContextNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;


/**
 * Utility class to get Context by ref in a tree of Context.
 * 
 * @author Xavier Courangon
 */
public final class ContextRef {

    /*
     * <path> ::= <absolute_path> | <relative_path>
     * <absolute_path> ::= '/' <relative_path>?
     * <relative_path> ::= <path_element> ('/' <path_element>)*
     * <path_element>  ::= '.' | '..' | (\w)+
     */
    private static final String  path_element  = "((\\.)|(\\.\\.)|(\\w+))";
    private static final String  relative_path = path_element + "(/" + path_element + ")*";
    private static final String  absolute_path = "/(" + relative_path + ")?";
    private static final String  path          = "(" + absolute_path + ")|(" + relative_path + ")";
    private static final Pattern pattern       = Pattern.compile(path);

    private ContextRef() {
    }

    public static void check(String contextRef) throws IllegalArgumentException {
        if (contextRef == null) {
            throw new IllegalArgumentException("contextRef is null");
        }

        if (pattern.matcher(contextRef).matches() == false) {
            throw new IllegalArgumentException("contextRef '" + contextRef + "' is not wellformed");
        }
    }

    public static Context getContext(Context context, String contextRef) throws ContextNotFound {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }

        check(contextRef);

        try {
            if (contextRef.equals("/")) {
                return getRoot((ContextNode)context);
            }
            final String split[] = contextRef.split("/");
            Context currentContext = context;
            for (String contextId : split) {
                switch (contextId) {
                    case "":
                        currentContext = getRoot((ContextNode)currentContext);
                        break;
                    case ".":
                        break;
                    case "..":
                        currentContext = ((ContextNode)currentContext).getParent();
                        break;
                    default:
                        try {
                            currentContext = ((ContextNode)currentContext).getSubcontext(contextId);
                        } catch (ContextNotFound e) {
                            throw new ContextNotFound(contextRef);
                        }
                }
            }
            return currentContext;
        } catch (ClassCastException e) {
            throw new ContextNotFound(contextRef);
        }
    }

    /**
     * @param context
     * @return
     */
    public static ContextNode getRoot(ContextNode context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        if (context.getParent() == null) {
            return context;
        } else {
            return getRoot(context.getParent());
        }
    }

    public static Collection<ContextNode> getAll(ContextNode root, String name) {
        if (root == null) {
            throw new IllegalArgumentException("root is null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
      if(name.contains("/")||name.contains(".")) {
        throw new IllegalArgumentException("context name'" + name + "' is not wellformed");
      }
 
     final Collection<ContextNode> all = new ArrayList<>();
     final Collection<ContextNode> subContexts =   root.getSubcontexts();
     for (final ContextNode context : subContexts) {
  		if(name.equals(context.getName())) {
        all.add(context);
      }
       all.addAll(getAll(context,name));
		}
     return all;
   }

    public static boolean isParent(ContextNode parent, ContextNode context) {
        if (parent == null || context == null) {
            throw new IllegalArgumentException("A least one of arguments is null");
        }
        ContextNode current = context.getParent();
        while (current != null) {
            if (current == parent) {
                return true;
            } else {
                current = context.getParent();
            }
        }
        return false;
    }
}

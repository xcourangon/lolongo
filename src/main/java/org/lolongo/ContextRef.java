package org.lolongo;

import org.lolongo.Context;
import org.lolongo.ContextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;


/**
 * Utility class to get Context by ref in a tree of Context.
 * 
 * @author Xavier Courangon
 */
public final class ContextRef {

    private static Logger        logger        = LoggerFactory.getLogger(ContextRef.class);

    /*
     * <path> ::= <absolute_path> | <relative_path>
     * <absolute_path> ::= '/' <relative_path>?
     * <relative_path> ::= <path_element> ('/' <path_element>)*
     * <path_element>  ::= '.' | '..' | <context_name>
     * <context_name>  ::= (\w)+
     */
    private static final String  context_name  = NamedContext.regex;
    private static final String  path_element  = "((\\.)|(\\.\\.)|(" + context_name + "))";
    private static final String  relative_path = path_element + "(/" + path_element + ")*";
    private static final String  absolute_path = "/(" + relative_path + ")?";
    private static final String  path          = "((" + absolute_path + ")|(" + relative_path + "))";
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

        ContextRef.check(contextRef);

        // "." is polymorphe
        if (contextRef.equals(".")) {
            return context;
        }
        final ContextNode contextNode = (ContextNode)context;
        final String path[] = split(contextRef);
        try {
            return getContext(contextNode, path);
        } catch (ContextNotFound _) {
            throw new ContextNotFound(contextRef);
        }
    }

    protected static String[] split(String contextRef) {
        String[] split = contextRef.split("/");
        if (contextRef.startsWith("/")) {
            if (split.length == 0) {
                split = new String[]{"/"};
            } else {
                assert split[0].equals("");
                split[0] = "/";
            }
        }
        return split;
    }

    protected static ContextNode getContext(ContextNode context, String[] path) throws ContextNotFound {
        int length = path.length;
        switch (length) {
            case 0:
                return context;
            default:
                final ContextNode contextNode = getContextPart(context, path[0]);
                final String[] nextPath = Arrays.copyOfRange(path, 1, length);
                return getContext(contextNode, nextPath);
        }
    }

    protected static ContextNode getContextPart(ContextNode context, String contextName) throws ContextNotFound {
        switch (contextName) {
            case "/":
                return context.getRoot();
            case ".":
                return context;
            case "..":
                return context.getParent();
            default:
                return context.getSubcontext(contextName);
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

    /**
     * <context_refs> ::= <path> (',' <path> )*
     **/
    private static final Pattern contextRefsPattern = Pattern.compile(path + "(\\s*,\\s*" + path + ")*");

    public static Collection<String> getContextRefList(String contextRefs) {
    	if (contextRefs == null) {
			return null;
      }

      if(contextRefsPattern.matcher(contextRefs).matches()==false) {
         throw new IllegalArgumentException("contextRefs '" + contextRefs + "' is not wellformed");
      }
       
     final String[] split = contextRefs.split(",");
      
     final Collection<String> result = new ArrayList<>(split.length);
     for (String contextRef : split) {
        contextRef=contextRef.trim();
        ContextRef.check(contextRef);
        result.add(contextRef);
		}
		return result;
	}
}

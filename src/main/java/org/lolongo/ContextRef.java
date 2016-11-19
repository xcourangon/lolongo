package org.lolongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to get Context by ref in a tree of Context.
 * 
 * @author Xavier Courangon
 */
public final class ContextRef {

    private static Logger logger = LoggerFactory.getLogger(ContextRef.class);

    public static final String path_separator = "/";
    public static final String root_context = path_separator;
    public static final String current_context = ".";
    public static final String parent_context = "..";
    /*
     * <path> ::= <absolute_path> | <relative_path>
     * <absolute_path> ::= '/' <relative_path>?
     * <relative_path> ::= <path_element> ('/' <path_element>)*
     * <path_element>  ::= '.' | '..' | <context_name>
     * <context_name>  ::= (\w)+
     */
    private static final String context_name = NamedContext.regex;
    private static final String path_element = "((\\Q" + current_context + "\\E)|(\\Q" + parent_context + "\\E)|(" + context_name + "))";
    private static final String relative_path = path_element + "(" + path_separator + path_element + ")*";
    private static final String absolute_path = path_separator + "(" + relative_path + ")?";
    private static final String path = "((" + absolute_path + ")|(" + relative_path + "))";
    private static final Pattern pattern = Pattern.compile(path);

    private ContextRef() {
    }

    /**
     * Check if a context ref (absolute or relative) is well-formed.
     * @param contextRef the context ref to check
     * @throws IllegalArgumentException if the context ref is not well-formed
     */
    public static void check(String contextRef) throws IllegalArgumentException {
        if (contextRef == null) {
            throw new IllegalArgumentException("contextRef is null");
        }

        if (pattern.matcher(contextRef).matches() == false) {
            throw new IllegalArgumentException("contextRef '" + contextRef + "' is not wellformed");
        }
    }

    /**
     * Returns the absolute context reference from a node Context.
     * @param contextNode the context we want its absolute ref
     * @return the absolute context reference 
     */
    public static String getAbsoluteContextRef(ContextNode contextNode) {
        final StringBuilder sb = new StringBuilder();
        String name = contextNode.getName();
        if (contextNode.isRoot()) {
            if (name != null) {
                sb.append(name);
            } else {
                sb.append(root_context);
            }
        } else {
            sb.append(getAbsoluteContextRef(contextNode.getParent()));
            sb.append(name);
            sb.append(path_separator);
        }
        return sb.toString();
    }

    public static Context getContext(Context context, String contextRef) throws ContextNotFound {

        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }

        ContextRef.check(contextRef);

        // current_context "." is polymorphe
        if (contextRef.equals(current_context)) {
            return context;
        }
        final ContextNode contextNode = (ContextNode) context;
        final String path[] = split(contextRef);
        try {
            return getContext(contextNode, path);
        } catch (ContextNotFound _) {
            throw new ContextNotFound(contextRef);
        }
    }

    protected static String[] split(String contextRef) {
        String[] split = contextRef.split(path_separator);
        if (contextRef.startsWith(path_separator)) {
            if (split.length == 0) {
                split = new String[] { path_separator };
            } else {
                assert split[0].equals("");
                split[0] = path_separator;
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
        case path_separator:
            return context.getRoot();
        case current_context:
            return context;
        case parent_context:
            return context.getParent();
        default:
            return context.getSubcontext(contextName);
        }
    }

    public static Collection<ContextNode> getAllSubcontext(ContextNode root, Predicate<ContextNode> predicate) {
        if (root == null) {
            throw new IllegalArgumentException("root is null");
        }
        if (predicate == null) {
            throw new IllegalArgumentException("predicate is null");
        }

        final Collection<ContextNode> result = new ArrayList<>();
        for (final ContextNode subContext : root.getSubcontexts()) {
            if (predicate.test(subContext)) {
                result.add(subContext);
            }
            result.addAll(getAllSubcontext(subContext, predicate));
        }
        return result;
    }

    public static Collection<ContextNode> getAllSubcontext(ContextNode root, final String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (name.contains(path_separator) || name.contains(current_context)) {
            throw new IllegalArgumentException("context name'" + name + "' is not wellformed");
        }

        return getAllSubcontext(root, new Predicate<ContextNode>() {
            @Override
            public boolean test(ContextNode t) {
                return t.getName().equals(name);
            }
        });
    }

    /**
     * <context_refs> ::= <path> (',' <path> )*
     **/
    private static final Pattern contextRefsPattern = Pattern.compile(path + "(\\s*,\\s*" + path + ")*");

    public static List<String> getContextRefList(String contextRefs) {
        if (contextRefs == null) {
            throw new IllegalArgumentException("contextRefs is null");
        }

        if (contextRefsPattern.matcher(contextRefs).matches() == false) {
            throw new IllegalArgumentException("contextRefs '" + contextRefs + "' is not wellformed");
        }

        final String[] split = contextRefs.split(",");

        final List<String> result = new ArrayList<>(split.length);
        for (String contextRef : split) {
            contextRef = contextRef.trim();
            ContextRef.check(contextRef);
            result.add(contextRef);
        }
        return result;
    }

}

package org.lolongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Context implementation for Context tree.
 * 
 * @author Xavier Courangon
 */
public class ContextNode extends NamedContext {

    private static Logger logger  = LoggerFactory.getLogger(ContextNode.class);

    private ContextNode   parent = null;
    private Map<String,ContextNode> subcontexts=new HashMap<>();
    private boolean inherit=true;
  
    public ContextNode(String id) {
      super(id);
    }

    public ContextNode(String id, boolean inherit) {
      this(id);
      this.inherit=inherit;
    }
  
    public ContextNode getParent() {
        return parent;
    }
  
  public boolean isRoot() {
    return parent==null;
  }

    public Collection<ContextNode> getSubcontexts() {
      return subcontexts.values();
    }
  
    public ContextNode getSubcontext(String name) throws ContextNotFound {
      if(subcontexts.containsKey(name)){
        return subcontexts.get(name);
      } else {
        throw new ContextNotFound(name);
      }
    }
  
    @Override
    public <T, R extends Ref<T>> T get(R ref) throws RefNotFound {
      return get(ref,true);
    }

      
    public <T, R extends Ref<T>> T get(R ref, boolean recursive) throws RefNotFound {
        try {
            return super.get(ref);
        } catch (RefNotFound e) {
            if (recursive && inherit && parent!=null) {
                try {
                    return parent.get(ref);
                } catch(RefNotFound _) {
                }
            }
            throw e;
        }
    }

    public void addSubcontext(ContextNode subcontext) throws ContextAlreadyExists{
      final String name=subcontext.getName();
      if(subcontexts.containsKey(name)) {
      	throw new ContextAlreadyExists(name);
      } else {
			subcontext.parent=this;
      	subcontexts.put(name,subcontext);
      }
    }
  
    public ContextNode getRoot() {
        if (this.isRoot()) {
            return this;
        } else {
          assert parent !=null;
          return parent.getRoot();
        }
    }

    public boolean isParentOf(ContextNode context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        final ContextNode parent = context.getParent();

        if (parent == null) {
            return false;
        }
        if (parent == this) {
            return true;
        }
        return isParentOf(parent);
    }
  
  public Context getContext(String contextRef) throws ContextNotFound {
    return ContextRef.getContext(this, contextRef);
  }

}

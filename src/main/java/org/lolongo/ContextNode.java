package org.lolongo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ContextNode extends NamedContext {

    private Context   parent = EmptyContext.getInstance();
    private Map<String,ContextNode> subcontexts=new HashMap<>();
    private boolean inherit=true;
  
    public ContextNode(String id) {
      super(id);
    }

    public ContextNode(String id, boolean inherit) {
      this(id);
      this.inherit=inherit;
    }

    public Context getParent() {
        return parent;
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
    public <T,R extends Ref<T>> T get(R ref) throws RefNotFound {

        try {
            return super.get(ref);
        } catch (RefNotFound e) {
            if (inherit) {
                try {
                    return parent.get(ref);
                } catch(RefNotFound forget) {
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
}

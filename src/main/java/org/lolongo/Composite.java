package org.lolongo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

public class Composite<T>  {

  private final Collection<T> components = new ArrayList<>();
    
  public void add(T component) {
    components.add(component);
  }

  public T create(Class<T> cl) {
        return (T) Proxy.newProxyInstance(cl.getClassLoader(), new Class[]{cl}, new InvocationHandler() {
            
            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
              Object returned=null;
      			for (T component : components) {
						returned = method.invoke(component, args);
					}
              return returned;
            }
        });
    }

}
